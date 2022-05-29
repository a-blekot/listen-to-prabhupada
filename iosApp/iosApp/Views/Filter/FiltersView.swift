//
//  FiltersView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FiltersView: View {
    private let component: FiltersComponent
    
    @EnvironmentObject var theme: Theme
    @ObservedObject
    private var models: ObservableValue<FiltersState>
    
    init(_ component: FiltersComponent) {
        self.component = component
        self.models = ObservableValue(component.models)
    }
    
    var body: some View {
        let model = models.value
        
        NavigationView {
            VStack(alignment: .center) {
                
                var chips = chips(model.filters)
                
                if !chips.isEmpty {
                    let _ = chips.append(ChipData(title: "Очистить все", clearAll: true))
                    SelectedFilters(chips: chips, component: component)
                }
                
                List(model.filters) { filter in
                    FilterItemView(filter: filter, component: component)
                }
                .edgesIgnoringSafeArea(.bottom)
                .edgesIgnoringSafeArea(.horizontal)
                //.listRowSeparator(Visibility.hidden)
                .listStyle(.plain)
                .animation(.easeOut(duration: 0.5))
                .navigationTitle("Фильтры")
                .navigationBarTitleDisplayMode(.inline)
                .toolbar {
                    Button("Применить") {
                        component.onApplyChanges()
                    }
                    .foregroundColor(theme.bodyTextColor)
                }
                
                Spacer()
                
                Text("Найдено \(model.totalLecturesCount) лекций")
                    .font(theme.buttonFont)
                    .foregroundColor(theme.bodyTextColor)
                    .frame(maxWidth: .infinity, minHeight: 50)
                    .background(theme.buttonMidBacground)
                    .contentShape(Rectangle())
                    .onTapGesture { component.onApplyChanges() }
            }
        }
    }
    
    private func chips(_ filters: [Filter]) -> [ChipData] {
        filters.map { ChipData(filter: $0) }.compactMap{ $0 }
    }
}


extension Filter: Identifiable {}

struct FiltersView_Previews: PreviewProvider {
    static var previews: some View {
        FiltersView(StubFiltersComponent())
            .environmentObject(themes[0])
    }
}
