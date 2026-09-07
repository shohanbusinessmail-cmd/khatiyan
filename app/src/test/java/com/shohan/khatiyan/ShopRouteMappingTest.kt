package com.shohan.khatiyan

import com.shohan.khatiyan.ui.navigation.Screen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guards the navigation route contract for the Shop (দোকান) module.
 *
 * A mismatch between the declared route pattern and the string produced by `createRoute()`
 * silently breaks navigation at runtime (the NavHost simply cannot resolve the destination),
 * so these mappings are asserted here.
 */
class ShopRouteMappingTest {

    @Test
    fun shopDetailRouteMatchesPattern() {
        assertEquals("shop_detail/{shopId}", Screen.ShopDetail.route)
        assertEquals("shop_detail/42", Screen.ShopDetail.createRoute(42L))
    }

    @Test
    fun editShopRouteIsRegisteredAndMatchesPattern() {
        assertEquals("edit_shop/{shopId}", Screen.EditShop.route)
        assertEquals("edit_shop/7", Screen.EditShop.createRoute(7L))
    }

    @Test
    fun addShopCreditRouteMatchesPattern() {
        assertEquals("add_shop_credit/{shopId}", Screen.AddShopCredit.route)
        assertEquals("add_shop_credit/3", Screen.AddShopCredit.createRoute(3L))
    }

    @Test
    fun addShopPaymentRouteMatchesPattern() {
        assertEquals("add_shop_payment/{shopId}", Screen.AddShopPayment.route)
        assertEquals("add_shop_payment/9", Screen.AddShopPayment.createRoute(9L))
    }

    @Test
    fun shopModuleRoutesAreUnique() {
        val routes = listOf(
            Screen.ShopList.route,
            Screen.ShopDetail.route,
            Screen.AddShop.route,
            Screen.EditShop.route,
            Screen.AddShopCredit.route,
            Screen.AddShopPayment.route
        )
        assertEquals(routes.size, routes.toSet().size)
    }

    @Test
    fun generatedRoutesNeverContainUnresolvedPlaceholders() {
        val generated = listOf(
            Screen.ShopDetail.createRoute(1L),
            Screen.EditShop.createRoute(1L),
            Screen.AddShopCredit.createRoute(1L),
            Screen.AddShopPayment.createRoute(1L)
        )
        generated.forEach { route ->
            assertTrue("Unresolved placeholder in route: $route", !route.contains("{"))
        }
    }
}
