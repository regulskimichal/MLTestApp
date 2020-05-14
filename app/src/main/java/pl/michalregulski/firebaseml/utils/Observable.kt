@file:Suppress("unused")

package pl.michalregulski.firebaseml.utils

import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField

open class Observable<T : Any?>(
    value: T,
    vararg dependencies: androidx.databinding.Observable
) : ObservableField<T>() {

    init {
        dependencies.forEach {
            it.addOnPropertyChangedCallback { _, _ ->
                notifyChange()
            }
        }
    }

    private var value = value
        private set(value) {
            if (field != value) {
                field = value
                notifyChange()
            }
        }

    override fun get(): T = value

    override fun set(value: T) {
        this.value = value
    }

    fun update(transform: T.() -> T) {
        value = transform(value)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun addOnPropertyChangedListener(crossinline callback: (value: T, propertyId: Int) -> Unit) =
        addOnPropertyChangedCallback { sender, propertyId ->
            if (sender is ObservableField<*>) {
                callback(sender.get() as T, propertyId)
            }
        }
}

inline fun androidx.databinding.Observable.addOnPropertyChangedCallback(
    crossinline callback: (sender: androidx.databinding.Observable?, propertyId: Int) -> Unit
) {
    addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: androidx.databinding.Observable?, propertyId: Int) {
            callback(sender, propertyId)
        }
    })
}

typealias ObservableBoolean = Observable<Boolean>

@Suppress("FunctionName")
fun ObservableBoolean(vararg dependencies: androidx.databinding.Observable): ObservableBoolean =
    Observable(false, *dependencies)

fun ObservableBoolean.negate() = update {
    this.not()
}

typealias ObservableChar = Observable<Char>

@Suppress("FunctionName")
fun ObservableChar(vararg dependencies: androidx.databinding.Observable): ObservableChar =
    Observable('\u0000', *dependencies)

typealias ObservableByte = Observable<Byte>

@Suppress("FunctionName")
fun ObservableByte(vararg dependencies: androidx.databinding.Observable): ObservableByte =
    Observable(0, *dependencies)

typealias ObservableShort = Observable<Short>

@Suppress("FunctionName")
fun ObservableShort(vararg dependencies: androidx.databinding.Observable): ObservableShort =
    Observable(0, *dependencies)

typealias ObservableInt = Observable<Int>

@Suppress("FunctionName")
fun ObservableInt(vararg dependencies: androidx.databinding.Observable): ObservableInt =
    Observable(0, *dependencies)

typealias ObservableLong = Observable<Long>

@Suppress("FunctionName")
fun ObservableLong(vararg dependencies: androidx.databinding.Observable): ObservableLong =
    Observable(0L, *dependencies)

typealias ObservableFloat = Observable<Float>

@Suppress("FunctionName")
fun ObservableFloat(vararg dependencies: androidx.databinding.Observable): ObservableFloat =
    Observable(0.0f, *dependencies)

typealias ObservableDouble = Observable<Double>

@Suppress("FunctionName")
fun ObservableDouble(vararg dependencies: androidx.databinding.Observable): ObservableDouble =
    Observable(0.0, *dependencies)
