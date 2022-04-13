import SwiftUI
import Combine
import shared
import AVFoundation

class Pager {
   var page = 1
}

struct ContentView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    let pager = Pager()
    
    var body: some View {
        NavigationView {
            listView()
                .navigationBarTitle("Lectures")
                .navigationBarItems(
                    trailing: Button(
                        "Reload",
                        action: {
                            incPage()
                            self.viewModel.loadLectures(page: pager.page)
                        })
                )
                .onAppear(perform: { self.viewModel.startObserving() } )
        }
    }
    
    func incPage() {
        pager.page += 1
    }
    
    private func listView() -> AnyView {
        switch viewModel.lectures {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let lectures):
            return AnyView(
                List(lectures) { lecture in
                    LectureListItem(lecture: lecture, isPlaying: false, onEvent: {e in viewModel.onEvent(e)})
                        .frame( maxWidth: .infinity)
                }
                    .frame(maxWidth: .infinity)
                    .padding(-10)
                    .edgesIgnoringSafeArea(.horizontal)
                    .edgesIgnoringSafeArea(.bottom)
            )
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
}

extension ContentView {
    enum LoadableLectures {
        case loading
        case result([Lecture])
        case error(String)
    }
    
    class ViewModel: ObservableObject {
        let repo: ResultsRepository
        
        var audioPlayer: AVPlayer?
        
        func onEvent(_ event: CommonUiEvent) {
            let player = event as? CommonUiEvent.Player
            let action = player?.action as? Play
            guard let id = action?.lectureId else { return }
            
            guard case .result(let arr) = lectures else { return }
            
            guard let url = arr.first(where: { $0.id == id })?.remoteUrl else { return }
            
            play(URL(string: url))
        }
        
        func play(_ url: URL?) {
            guard let url = url else { return }
            
            //            let url = URL(string: "https://s3.amazonaws.com/kargopolov/kukushka.mp3")
            let playerItem:AVPlayerItem = AVPlayerItem(url: url)
            
            do {
                audioPlayer = try AVPlayer(playerItem: playerItem)
                audioPlayer?.play()
            } catch let error as NSError {
                print("Unresolved error \(error.debugDescription)")
            }
            
            //            works but blocks UI thread
//            if let data = NSData(contentsOf: url) {
//                do {
//                    try AVAudioSession.sharedInstance().setCategory(AVAudioSession.Category.playback, options: .allowBluetooth)
//                    try AVAudioSession.sharedInstance().setActive(true)
//                    audioPlayer = try AVAudioPlayer(data: data as Data)//, fileTypeHint: AVFileTypeAppleM4A)
//                    audioPlayer!.prepareToPlay()
//                    audioPlayer!.play()
//
//                } catch let error as NSError {
//                    print("Unresolved error \(error.debugDescription)")
//                }
//            }
            
//            do not work ...
//            do {
//                audioPlayer = try AVAudioPlayer(contentsOf: url)
//                audioPlayer?.play()
//            } catch {
//                print("couldn't load file :(")
//                print("Unexpected error: \(error).")
//            }
        }
        
        @Published var lectures = LoadableLectures.loading
        
        init(repo: ResultsRepository) {
            self.repo = repo
            loadLectures(page: 1)
        }
        
        func startObserving() {
            self.repo
                .observeState()
                .watch { state in
                    guard let state = state else { return }
                    self.lectures = self.convert(state)
                }
        }
        
        func convert(_ state: ResultsState) -> LoadableLectures {
            switch true {
            case state.isLoading: return .loading
            case state.lectures.isEmpty: return .error("Empty")
            default: return .result(state.lectures)
            }
        }
        
        func loadLectures(page: Int) {
            self.lectures = .error("Error")
            
            //            do {
            repo.updatePage(page: Int32(page), completionHandler: { results, error in
                
                guard let error = error else {return}
                print(error.localizedDescription)
            })
//            } catch {
//                print("Error")
//            }
            
//            repo.getResults(page: Int32(page), completionHandler: { results, error in
//
//                if let results = results {
//                    self.lectures = .result(results.toLect())
//                } else {
//                    self.lectures = .error(error?.localizedDescription ?? "error")
//                }
//            })
        }
    }
}

class Collector<T> : Kotlinx_coroutines_coreFlowCollector {
    let callback:(T) -> Void
    
    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }
    
    func emit(value: Any?) async throws -> KotlinUnit {
        if let v = value as? T {
            callback(v)
        }
        return KotlinUnit()
    }
}

extension Lecture: Identifiable { }

