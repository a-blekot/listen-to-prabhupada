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
            VStack(alignment: .center, spacing: theme.dimens.paddingXS) {
                HStack(spacing: theme.dimens.paddingM) {
                    Image.init(systemName: "arrow.backward")
                        .resizable()
                        .frame(width: theme.dimens.iconSizeS, height: theme.dimens.iconSizeS, alignment: .center)
                        .padding(.leading, theme.dimens.paddingM)
                        .onTapGesture { component.onApplyChanges() }
                    
                    Text("Найдено: \(model.totalLecturesCount) лекций")
                        .font(theme.titleLarge)
                        .foregroundColor(theme.colors.filtersText)
                        .lineLimit(1)
                    
                    Spacer()
                }
                .frame(height: theme.dimens.toolbarHeightL)
                .background(theme.colors.toolbar)
                .padding(.bottom, theme.dimens.paddingS)
                
                var chips = chips(model.filters)
                
                if !chips.isEmpty {
                    let _ = chips.append(ChipData(title: "Очистить все", clearAll: true))
                    SelectedFilters(chips: chips, component: component)
                        .padding(.horizontal, theme.dimens.horizontalScreenPadding - 4)
                        .padding(.bottom, theme.dimens.paddingS)
                }
                
                ScrollView {
                    ForEach(model.filters) { filter in
                        FilterItemView(filter: filter, component: component)
                            .animation(.easeInOut(duration: 0.4))
                    }
                }
                .padding(.horizontal, theme.dimens.horizontalScreenPadding)
                .edgesIgnoringSafeArea(.bottom)
                .edgesIgnoringSafeArea(.horizontal)
                .navigationBarHidden(true)
                .toolbar {
                    Button("Применить") {
                        component.onApplyChanges()
                    }
                    .foregroundColor(theme.colors.filtersText)
                }
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
