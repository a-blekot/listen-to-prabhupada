import SwiftUI
import Combine
import shared

struct ContentView: View {
	let greet = Greeting().greeting()
    
	var body: some View {
        VStack(alignment: HorizontalAlignment.leading, spacing: 10.0) {
        
            Text(greet)
                .font(.title)
                .fontWeight(/*@START_MENU_TOKEN@*/.bold/*@END_MENU_TOKEN@*/)
                .foregroundColor(/*@START_MENU_TOKEN@*/.yellow/*@END_MENU_TOKEN@*/)
                .padding(5.0)
            
            Text("Green Rock")
                .font(.title)
                .foregroundColor(.green)
                .fontWeight(.bold)
                .padding(5.0)
            
            UserList(users: getUsers())
        }
        .padding(20)
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

struct UserList : View {
    
    var users: [UserPreview]
    
    var body: some View {
        List(users, id: \.id) { user in
            User(user: user)
        }
    }
}

struct User: View {
    
    var user: UserPreview
    
    var body: some View {
        HStack(alignment: .center, spacing: 10.0) {
            ImageView(withURL: user.picture)
            
            VStack(alignment: .leading, spacing: 10.0) {
                Text(user.id)
                Text(user.fullName)
            }
        }
    }
}

func getUsers() -> Array<UserPreview> {
    var users = Array<UserPreview>()
    users.append(getUser(id: "1", title: "Mr", first: "Vasja1", last: "One"))
    users.append(getUser(id: "2", title: "Mr", first: "Vasja2", last: "Two"))
    users.append(getUser(id: "3", title: "Mr", first: "Vasja3", last: "Three"))
    
    return users
}

func getUser(id: String, title: String, first: String, last: String) -> UserPreview {
    return UserPreview(id: id, title: title, firstName: first, lastName: last, picture: "https://picsum.photos/100")
}

//func getImageView(url: String) -> UIImageView {
//    let imageView = UIImageView()
//
//    imageView.load(url: url)
//    return imageView
//}

//    .previewLayout(.fixed(width: 300, height: 70))


class ImageLoader: ObservableObject {
    var didChange = PassthroughSubject<Data, Never>()
    var data = Data() {
        didSet {
            didChange.send(data)
        }
    }

    init(urlString:String) {
        guard let url = URL(string: urlString) else { return }
        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            guard let data = data else { return }
            DispatchQueue.main.async {
                self.data = data
            }
        }
        task.resume()
    }
}

struct ImageView: View {
    @ObservedObject var imageLoader:ImageLoader
    @State var image:UIImage = UIImage()

    init(withURL url:String) {
        imageLoader = ImageLoader(urlString:url)
    }

    var body: some View {
            Image(uiImage: image)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width:50, height:50)
                .clipShape(Circle())
                .shadow(radius: 7)
                .onReceive(imageLoader.didChange) { data in
                   self.image = UIImage(data: data) ?? UIImage()
                }
    }
}
