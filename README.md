# Android Mock Api Server

It is a framework that provides local data and allows you to test your app without a backend.

### Features

- Mock response using json files, data class object or string bodies
- Mock response sequence
- Provide response delay time
- Provide http response code

### Installation

Inside the dependency block in the `build.gradle` file of your application, add this line to add the library:
```Groovy
repositories {
    mavenCentral()
}
dependencies {
    ...
    implementation 'com.telefonica:mock:$version'
    ...
}
```

Create a demo variant in order to only have json files for development purposes:
```Gradle
flavorDimensions "environment"

productFlavors {
    ...
    demo {
        dimension "environment"
    }
    ...
}
```
Create an asset folder in demo variant and place there all json files.

Provide a singleton instance of MockHelper using dependency injection:
```Kotlin
@Provides
@Singleton
fun provideMockHelper(
    @ApplicationContext context: Context
): MockHelper = MockHelper(context)
```

Inject MockHelper in Application file. Then in live cycle methods, start, stop and provide mocks.
```Kotlin
class App : Application() {
    
    ...
    @Inject
    lateinit var mockHelper: MockHelper
    ...

    override fun onCreate() {
        ...
        if (BuildConfig.DEFAULT_ENVIRONMENT == Environment.DEMO) {
            CoroutineScope(Dispatchers.IO).launch {
                mockHelper.startServer()
                mockHelper.provideDispatcher(getUserMocksUseCase()) // This use case provide a List<Mock>
            }
        }
        ...
    }
 
    override fun onTerminate() {
        super.onTerminate()
        if (BuildConfig.DEFAULT_ENVIRONMENT == Environment.DEMO) {
            mockHelper.stopServer()
        }
    }
}
```

At some point in the application, where the app set the base URL to make requests, you have to check if current variant is "demo" and set the base url obtained from method:

```Kotlin
mockHelper.getBaseUrl()
```

### Demo App

Demo app shows a list of users in two screens, first shows five users, second shows ten users.
To use mock data, select demoDebug variant:
- The first list, is a json sequence which the first request is success and the second request is an error.
- The second list, is a data object response.

To use real data, select preDebug variant

