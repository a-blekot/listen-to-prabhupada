import SwiftUI
import shared

@main
struct iOSApp: App {
    @StateObject private var modelData = ModelData()

    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: .init(repo: repo))
                .environmentObject(modelData)
        }
    }
    
    var repo: ResultsRepository {
        return ResultsRepositoryImpl(
            db: DatabaseImpl(databaseDriverFactory:DatabaseDriverFactory()),
            api: PrabhupadaApiImpl(client: KtorClientFactory().build()),
            playbackRepository: PlaybackRepositoryImpl(),
            withLog: false
        )
    }
}
