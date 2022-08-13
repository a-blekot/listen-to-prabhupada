//
//  PageControlSliderView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 29.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

class ObservablePage: ObservableObject {
    @Published var value: Double = 0.0
}

extension Pagination {
    var progress: Double {
        Double(curr) / Double(total)
    }
}

struct PageControlSliderView: View {
    @EnvironmentObject var theme: Theme
    @ObservedObject private var page = ObservablePage()
    private let pagination: Pagination
    private let component : ResultsComponent
    
    init(_ page: ObservablePage, _ pagination: Pagination, _ component: ResultsComponent) {
        self.page = page
        self.pagination = pagination
        self.component = component
    }
    
    var body: some View {
        
        HStack(alignment: .center, spacing: 4) {
            Text("\(Int(page.value)) из \(pagination.total)")
                .font(theme.titleSmall)
                .foregroundColor(theme.colors.tertiary)
                .lineLimit(1)
                .padding(.horizontal, 2)
                .frame(width: 75)
            
            Slider(
                value: $page.value,
                in: 1.0...Double(pagination.total),
                step: 1.0,
                onEditingChanged: sliderEditingChanged,
                label: { Text("Go to page") }
            )
                .accentColor(.orange)
                .padding()
                .foregroundColor(theme.colors.tertiary)
                .frame(height: 40)
            
            
        }
    }
    
    private func sliderEditingChanged(editingStarted: Bool) {
        if !editingStarted {
            print()
            component.onPage(page: Int32(page.value))
        }
    }
}

struct PageControlSliderView_Previews: PreviewProvider {
    static var previews: some View {
        ResultsView(StubResultsComponent())
            .environmentObject(themes[0])
    }
}
