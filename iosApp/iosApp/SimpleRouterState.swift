//
//  SimpleRouterState.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Prabhupada

func simpleChildStack<T : AnyObject>(_ child: T) -> Value<ChildStack<AnyObject, T>> {
    return valueOf(
        ChildStack(
            configuration: "config" as AnyObject,
            instance: child
        )
    )
}

