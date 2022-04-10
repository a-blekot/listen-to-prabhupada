import SwiftUI
import Combine
import shared

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
            return AnyView(List(lectures) { lecture in
                LectureListItem(lecture: lecture)
            })
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
        
        @Published var lectures = LoadableLectures.loading
        
        init(repo: ResultsRepository) {
            self.repo = repo
            //            self.repo.doInit(shareAction: nil, completionHandler: {unit, error in})
            
            loadLectures(page: 1)
            
            //            self.repo
            //                .observeState()
            //                .collect(
            //                    collector: Collector<ResultsState>{ v in
            //                        print("result \(v)")
            //                        self.lectures = .result(v.lectures)
            //                    }
            //                ) { kotlinUnit, possibleError in
            //                    print("finished with possible error")
            //                    self.lectures = .error(possibleError?.localizedDescription ?? "error")
            //                }
            //                .asCommonFlow()
            //                .watch { color in
            //                    guard let colorHex = color?.hex else {
            //                        return
            //                    }
            //                    self.colorBehaviourSubject.onNext(colorHex)
            //                }
        }
        
        func loadLectures(page: Int) {
            self.lectures = .loading
            repo.getResults(page: Int32(page), completionHandler: { results, error in
                
                if let results = results {
                    self.lectures = .result(results.toLect())
                } else {
                    self.lectures = .error(error?.localizedDescription ?? "error")
                }
            })
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

