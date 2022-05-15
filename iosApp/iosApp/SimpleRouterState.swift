//
//  SimpleRouterState.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Prabhupada

func simpleRouterState<T : AnyObject>(_ child: T) -> Value<RouterState<AnyObject, T>> {
    return valueOf(
        RouterState(
            activeChild: ChildCreated(
                configuration: "config" as AnyObject,
                instance: child
            ),
            backStack: []
        )
    )
}
