

package com.a.b {
    package object horstmann {
        val m = 1
    }
    package horstmann {
        object PackageDemo {
            val i = m
        }
    }
}
