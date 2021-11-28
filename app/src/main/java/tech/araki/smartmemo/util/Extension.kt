package tech.araki.smartmemo.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

/**
 * [viewId]上に表示されているソフトウェアキーボードを隠す
 */
fun Activity.hideSoftwareKeyboard(@IdRes viewId: Int) {
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(this.findViewById<View>(viewId).windowToken, 0)
}

/**
 * Toastを生成する
 */
fun makeToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

/** Memo環境設定 (どのContextからもアクセスできるようにTop-levelに設置することを推奨) */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/** ソート方法に関する設定 */
val PREFERENCES_KEY_SORT_SETTING = intPreferencesKey("sort_setting")