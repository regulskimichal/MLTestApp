@file:Suppress("unused")

package pl.michalregulski.firebaseml

import androidx.databinding.Observable

open class ObservableField<T : Any?> : androidx.databinding.ObservableField<T> {

    constructor()

    constructor(value: T) : super(value)

    constructor(vararg dependencies: Observable) : super(*dependencies)

    @Suppress("UNCHECKED_CAST")
    override fun get(): T = super.get() as T

    @Suppress("RedundantOverride")
    override fun set(value: T) = super.set(value)

}

inline fun <T> ObservableField<T>.update(transform: T.() -> Unit) {
    val value = get()
    if (value != null) {
        transform(value)
        notifyChange()
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <T> ObservableField<T>.addOnPropertyChanged(crossinline callback: (T) -> Unit) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, i: Int) {
            if (observable is ObservableField<*>) {
                callback(observable.get() as T)
            }
        }
    })
}

fun ObservableBoolean.negate() = set(get().not())

typealias ObservableBoolean = ObservableField<Boolean>

typealias ObservableChar = ObservableField<Char>

typealias ObservableByte = ObservableField<Byte>

typealias ObservableShort = ObservableField<Short>

typealias ObservableInt = ObservableField<Int>

typealias ObservableLong = ObservableField<Long>

typealias ObservableFloat = ObservableField<Float>

typealias ObservableDouble = ObservableField<Double>

typealias ObservableString = ObservableField<String>
