import SwiftUI
import shared

@main
struct iOSApp: App {

    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: .init(repo: repo))
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
