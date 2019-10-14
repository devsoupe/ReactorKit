package com.perelandrax.reactorkit.extras

import android.os.Build
import android.util.Log
import com.perelandrax.reactorkit.extras.StackTraceTagCreator.getFormattedTag
import io.reactivex.*
import io.reactivex.subjects.PublishSubject
import java.io.PrintWriter
import java.io.StringWriter
import java.util.regex.Pattern

/**
 * A [io.reactivex.Observable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnDispose)
 *
 * @param valueConverter function that will transform an onNext value for a log
 */
fun <E : Any> Observable<E>.debug(valueConverter: ((E) -> Any)): Observable<E> {
  val formattedTag = getFormattedTag(null)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Observable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 * @param valueConverter function that will transform an onNext value for a log
 */
fun <E : Any> Observable<E>.debug(tag: String, valueConverter: ((E) -> Any)): Observable<E> {
  val formattedTag = getFormattedTag(tag)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Observable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnDispose)
 */
fun <E : Any> Observable<E>.debug(): Observable<E> {
  val formattedTag = getFormattedTag(null)
  val valueConverterSafe = returnSelf<E>()
  return debugInternal(formattedTag, valueConverterSafe)
}

/**
 * A [io.reactivex.Observable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 */
fun <E : Any> Observable<E>.debug(tag: String): Observable<E> {
  val formattedTag = getFormattedTag(tag)
  val valueConverterSafe = returnSelf<E>()
  return debugInternal(formattedTag, valueConverterSafe)
}

/**
 * A [io.reactivex.Flowable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnCancel)
 */
fun <E : Any> Flowable<E>.debug(): Flowable<E> {
  val formattedTag = getFormattedTag(null)
  val valueConverter = returnSelf<E>()
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Flowable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnCancel)
 *
 * @param tag additional tag that is added to the log
 */
fun <E : Any> Flowable<E>.debug(tag: String): Flowable<E> {
  val formattedTag = getFormattedTag(tag)
  val valueConverter = returnSelf<E>()
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Flowable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnCancel)
 *
 * @param valueConverter function that will transform an onNext value for a log
 */
fun <E : Any> Flowable<E>.debug(valueConverter: ((E) -> Any)): Flowable<E> {
  val formattedTag = getFormattedTag(null)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Flowable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnNext, doOnError, doOnComplete, doOnTerminate, doOnCancel)
 *
 * @param tag additional tag that is added to the log
 * @param valueConverter function that will transform an onNext value for a log
 */
fun <E : Any> Flowable<E>.debug(tag: String, valueConverter: ((E) -> Any)): Flowable<E> {
  val formattedTag = getFormattedTag(tag)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Single] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnDispose)
 */
fun <E : Any> Single<E>.debug(): Single<E> {
  val formattedTag = getFormattedTag(null)
  val valueConverter = returnSelf<E>()
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Single] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 */
fun <E : Any> Single<E>.debug(tag: String): Single<E> {
  val formattedTag = getFormattedTag(tag)
  val valueConverter = returnSelf<E>()
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Single] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnDispose)
 *
 * @param valueConverter function that will transform an onSuccess value for a log
 */
fun <E : Any> Single<E>.debug(valueConverter: ((E) -> Any)): Single<E> {
  val formattedTag = getFormattedTag(null)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Single] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 * @param valueConverter function that will transform an onSuccess value for a log
 */
fun <E : Any> Single<E>.debug(tag: String, valueConverter: ((E) -> Any)): Single<E> {
  val formattedTag = getFormattedTag(tag)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Maybe] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnComplete, doOnDispose)
 */
fun <E : Any> Maybe<E>.debug(): Maybe<E> {
  val formattedTag = getFormattedTag(null)
  val valueConverter = returnSelf<E>()
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Maybe] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnComplete, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 */
fun <E : Any> Maybe<E>.debug(tag: String): Maybe<E> {
  val formattedTag = getFormattedTag(tag)
  val valueConverter = returnSelf<E>()
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Maybe] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnComplete, doOnDispose)
 *
 * @param valueConverter function that will transform an onSuccess value for a log
 */
fun <E : Any> Maybe<E>.debug(valueConverter: ((E) -> Any)): Maybe<E> {
  val formattedTag = getFormattedTag(null)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Maybe] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnSuccess, doOnError, doOnComplete, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 * @param valueConverter function that will transform an onSuccess value for a log
 */
fun <E : Any> Maybe<E>.debug(tag: String, valueConverter: ((E) -> Any)): Maybe<E> {
  val formattedTag = getFormattedTag(tag)
  return debugInternal(formattedTag, valueConverter)
}

/**
 * A [io.reactivex.Completable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnError, doOnComplete, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 */
fun Completable.debug(): Completable {
  val formattedTag = getFormattedTag(null)
  return debugInternal(formattedTag)
}

/**
 * A [io.reactivex.Completable] debug extension that will log an useful information from this stream
 *
 * Logged events: (doOnSubscribe, doOnError, doOnComplete, doOnDispose)
 *
 * @param tag additional tag that is added to the log
 */
fun Completable.debug(tag: String): Completable {
  val formattedTag = getFormattedTag(tag)
  return debugInternal(formattedTag)
}

/**
 * Global settings of RxDebug extensions
 */
object RxDebug {

  internal val isLoggingEnabled: Boolean
    get() = isLoggingEnabledInternal

  private var isLoggingEnabledInternal = true

  /**
   * Globally enable/disable logs for [debug] methods
   *
   * @param isLoggindEnabled true if logging should be enabled, false otherwise
   */
  fun setLoggingEnabled(isLoggindEnabled: Boolean) {
    this.isLoggingEnabledInternal = isLoggindEnabled
  }
}

internal fun <E : Any> PublishSubject<E>.debugInternal(tag: String, valueConverter: ((E) -> Any)): Observable<E> {
  return doOnNext { DebugLogger.log("OnNext", value = it, valueConverter = valueConverter, tag = tag) }
    .doOnError { DebugLogger.log("OnError", error = it, tag = tag) }
    .doOnSubscribe { DebugLogger.log("OnSubscribe", tag = tag) }
    .doOnDispose { DebugLogger.log("OnDispose", tag = tag) }
    .doOnTerminate { DebugLogger.log("OnTerminate", tag = tag) }
    .doOnComplete { DebugLogger.log("OnComplete", tag = tag) }
}

internal fun <E : Any> Observable<E>.debugInternal(tag: String, valueConverter: ((E) -> Any)): Observable<E> {
  return doOnNext { DebugLogger.log("OnNext", value = it, valueConverter = valueConverter, tag = tag) }
    .doOnError { DebugLogger.log("OnError", error = it, tag = tag) }
    .doOnSubscribe { DebugLogger.log("OnSubscribe", tag = tag) }
    .doOnDispose { DebugLogger.log("OnDispose", tag = tag) }
    .doOnTerminate { DebugLogger.log("OnTerminate", tag = tag) }
    .doOnComplete { DebugLogger.log("OnComplete", tag = tag) }
}

internal fun <E : Any> Flowable<E>.debugInternal(tag: String, valueConverter: ((E) -> Any)): Flowable<E> {
  return doOnNext { DebugLogger.log("OnNext", value = it, valueConverter = valueConverter, tag = tag) }
    .doOnError { DebugLogger.log("OnError", error = it, tag = tag) }
    .doOnSubscribe { DebugLogger.log("OnSubscribe", tag = tag) }
    .doOnTerminate { DebugLogger.log("OnTerminate", tag = tag) }
    .doOnComplete { DebugLogger.log("OnComplete", tag = tag) }
    .doOnCancel { DebugLogger.log("OnCancel", tag = tag) }
}

internal fun <E : Any> Single<E>.debugInternal(tag: String, valueConverter: ((E) -> Any)): Single<E> {
  return doOnSuccess { DebugLogger.log("OnSuccess", value = it, valueConverter = valueConverter, tag = tag) }
    .doOnError { DebugLogger.log("OnError", error = it, tag = tag) }
    .doOnSubscribe { DebugLogger.log("OnSubscribe", tag = tag) }
    .doOnDispose { DebugLogger.log("OnDispose", tag = tag) }
}

internal fun <E : Any> Maybe<E>.debugInternal(tag: String, valueConverter: ((E) -> Any)): Maybe<E> {
  return doOnSuccess { DebugLogger.log("OnSuccess", value = it, valueConverter = valueConverter, tag = tag) }
    .doOnError { DebugLogger.log("OnError", error = it, tag = tag) }
    .doOnSubscribe { DebugLogger.log("OnSubscribe", tag = tag) }
    .doOnComplete { DebugLogger.log("OnComplete", tag = tag) }
    .doOnDispose { DebugLogger.log("OnDispose", tag = tag) }
}

internal fun Completable.debugInternal(tag: String): Completable {
  return doOnError { DebugLogger.log("OnError", error = it, tag = tag) }
    .doOnComplete { DebugLogger.log("OnComplete", tag = tag) }
    .doOnSubscribe { DebugLogger.log("OnSubscribe", tag = tag) }
    .doOnDispose { DebugLogger.log("OnDispose", tag = tag) }
}

internal fun <E : Any> returnSelf(): (E) -> Any {
  return { it }
}

internal object StackTraceTagCreator {

  private const val MAX_TAG_LENGTH = 23

  private const val CALL_STACK_INDEX = 3

  private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

  fun getFormattedTag(tag: String?): String {
    return if (tag == null) {
      getStacktraceTag(atIndex = CALL_STACK_INDEX)
    } else {
      val stackTraceTag = getStacktraceTag(atIndex = CALL_STACK_INDEX)
      "$stackTraceTag: $tag"
    }
  }

  private fun getStacktraceTag(atIndex: Int): String {
    val stackTrace = Throwable().stackTrace
    if (stackTrace.size <= atIndex) {
      throw IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?")
    }
    return createStackElementTag(stackTrace[atIndex])
  }

  private fun createStackElementTag(element: StackTraceElement): String {
    var tag = element.className
    val m = StackTraceTagCreator.ANONYMOUS_CLASS.matcher(tag)
    if (m.find()) {
      tag = m.replaceAll("")
      tag = tag.substring(0, tag.lastIndexOf('$'))
    }
    tag = tag.substring(tag.lastIndexOf('.') + 1)

    // Tag length limit was removed in API 24.
    return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      tag
    } else {
      tag.substring(0, MAX_TAG_LENGTH)
    }
  }
}

internal object DebugLogger {

  private const val MAX_LOG_LENGTH = 4000

  fun log(title: String, tag: String) {
    if (RxDebug.isLoggingEnabled.not()) {
      return
    }

    logInternal(title, "", tag)
  }

  fun <E> log(title: String, value: E, valueConverter: (E) -> Any, tag: String) {
    if (RxDebug.isLoggingEnabled.not()) {
      return
    }

    val message = valueConverter(value).toString()
    logInternal(title, message, tag)
  }

  fun log(title: String, error: Throwable, tag: String) {
    if (RxDebug.isLoggingEnabled.not()) {
      return
    }

    val message = getStackTraceString(error)
    logInternal(title, message, tag)
  }

  private fun logInternal(title: String, message: String, tag: String) {
    val formattedTitle = if (message.isEmpty()) title else "$title: "
    val maxLength = MAX_LOG_LENGTH - formattedTitle.length
    if (message.length < maxLength) {
      Log.d(tag, "$formattedTitle$message")
      return
    }

    // Split by line, then ensure each line can fit into Log's maximum length.
    var i = 0
    val length = message.length
    while (i < length) {
      var newline = message.indexOf('\n', i)
      newline = if (newline != -1) newline else length
      do {
        val end = Math.min(newline, i + maxLength)
        val part = message.substring(i, end)
        Log.d(tag, "$formattedTitle$part")
        i = end
      } while (i < newline)
      i++
    }
  }

  private fun getStackTraceString(t: Throwable): String {
    val sw = StringWriter(256)
    val pw = PrintWriter(sw, false)
    t.printStackTrace(pw)
    pw.flush()
    return sw.toString()
  }
}