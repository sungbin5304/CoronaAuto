package me.sungbin.coronaauto.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.jsoup.Jsoup
import javax.inject.Singleton


/**
 * Created by SungBin on 2020-09-11.
 */

@Module
@InstallIn(ApplicationComponent::class)
class ApiClient {

    private val BASE_URL = "http://hw3235.herokuapp.com/"

    @Provides
    @Singleton
    fun instance() = Jsoup.connect(BASE_URL).ignoreContentType(true).ignoreHttpErrors(true)!!
}