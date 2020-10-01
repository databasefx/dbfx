package cn.navigational.dbfx.navigator.scheme

import cn.navigational.dbfx.config.SCHEME_ICON
import cn.navigational.dbfx.navigator.SchemeItem

class PgSchemeItem(scheme: String) : SchemeItem(SCHEME_ICON, scheme) {
    override suspend fun initData() {

    }
}