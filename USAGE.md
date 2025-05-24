# Revaltronics Commons - Usage Guide

This comprehensive guide provides detailed instructions for using the Revaltronics Commons library in your Android projects.

## Installation

### Step 1: Add JitPack Repository

Add the JitPack repository to your project's `build.gradle.kts` (Project level):

```kotlin
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Or in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add Dependency

Add the library dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.sunnybhadana:autophone-commons:v1.0.0")
}
```

### Alternative Installation Methods

#### For Gradle (Groovy)
```gradle
// In root build.gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

// In app/build.gradle
dependencies {
    implementation 'com.github.sunnybhadana:autophone-commons:v1.0.0'
}
```

## Available Versions

- **v1.0.0** - Initial release with comprehensive Android utilities

## Core Features

### 1. Base Activities

The library provides base activities with common functionality:

```kotlin
import com.revaltronics.commons.activities.BaseSimpleActivity

class MainActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Built-in functionality includes:
        // - Theme management
        // - Permission handling
        // - Common UI setup
    }
}
```

### 2. Extensions

Powerful Kotlin extensions for common operations:

```kotlin
import com.revaltronics.commons.extensions.*

// String extensions
val formattedNumber = phoneNumber.formatPhoneNumber()
val maskedEmail = email.maskEmail()
val safeString = nullableString.orEmpty()

// View extensions
view.fadeIn()
view.fadeOut()
view.setVisibleIf(condition)
view.gone()
view.visible()

// Context extensions
context.showToast("Message")
context.hasPermission(Manifest.permission.CAMERA)
context.openAppSettings()
```

### 3. Helper Classes

Comprehensive utility classes:

```kotlin
import com.revaltronics.commons.helpers.*

// File operations
val fileHelper = FileHelper(context)
fileHelper.copyFile(sourceFile, destinationFile)
val fileSize = fileHelper.getFileSize(file)

// Network utilities
if (NetworkHelper.isNetworkAvailable(context)) {
    // Perform network operation
}

// Database helpers
val dbHelper = DatabaseHelper(context)
dbHelper.exportDatabase()
```

### 4. Custom Views

Ready-to-use custom views:

```xml
<!-- Custom EditText with validation -->
<com.revaltronics.commons.views.ValidatedEditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:validationType="email"
    app:errorMessage="Please enter a valid email" />

<!-- Custom buttons with enhanced functionality -->
<com.revaltronics.commons.views.ThemedButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Custom Button"
    app:cornerRadius="8dp" />
```

### 5. Dialogs and Alerts

Pre-built dialog components:

```kotlin
import com.revaltronics.commons.dialogs.*

// Confirmation dialog
ConfirmationDialog(this) {
    title = "Delete Item"
    message = "Are you sure you want to delete this item?"
    positiveButton = "Delete"
    negativeButton = "Cancel"
    onPositive = { 
        // Handle deletion
        deleteItem()
    }
}.show()

// Input dialog
InputDialog(this) {
    title = "Enter Name"
    hint = "Your name"
    onConfirm = { input ->
        // Handle input
        saveName(input)
    }
}.show()
```

### 6. Adapters

Powerful base adapters for RecyclerView:

```kotlin
import com.revaltronics.commons.adapters.BaseRecyclerAdapter

class MyAdapter : BaseRecyclerAdapter<MyItem, MyViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    // Built-in features:
    // - Item animations
    // - Selection handling
    // - Click listeners
    // - Drag & drop support
}
```

### 7. Jetpack Compose Support

Modern Compose utilities and components:

```kotlin
import com.revaltronics.commons.compose.*

@Composable
fun MyScreen() {
    Column {
        // Custom loading button
        LoadingButton(
            text = "Submit",
            isLoading = isSubmitting,
            onClick = { handleSubmit() }
        )
        
        // Themed text field
        ThemedTextField(
            value = textValue,
            onValueChange = { textValue = it },
            label = "Enter text",
            isError = hasError
        )
        
        // Custom card with elevation
        ThemedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp
        ) {
            Text("Card content")
        }
    }
}
```

### 8. Database Support

Room database utilities and helpers:

```kotlin
import com.revaltronics.commons.database.*

// Use provided database helpers
@Entity(tableName = "my_table")
data class MyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val timestamp: Long
)

@Dao
interface MyDao : BaseDao<MyEntity> {
    // BaseDao provides common CRUD operations:
    // insert(), update(), delete(), getAll()
    
    @Query("SELECT * FROM my_table WHERE name LIKE :name")
    suspend fun findByName(name: String): List<MyEntity>
}

// Database configuration
@Database(
    entities = [MyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao
}
```

### 9. Models and Data Classes

Predefined data models for common use cases:

```kotlin
import com.revaltronics.commons.models.*

// Contact information
val contact = Contact(
    name = "John Doe",
    phoneNumber = "+1234567890",
    email = "john@example.com"
)

// App configuration
val config = AppConfig(
    theme = ThemeType.DARK,
    language = "en",
    notifications = true
)

// File information
val fileInfo = FileInfo(
    path = "/storage/file.txt",
    size = 1024L,
    lastModified = System.currentTimeMillis()
)
```

## Library Modules

1. **commons** - Main library module with core functionality
2. **strings** - String resources and localization support
3. **samples** - Sample application demonstrating library usage

## Configuration

### Application Setup

Configure the library in your Application class:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize commons configuration
        CommonsConfig.apply {
            primaryColor = ContextCompat.getColor(this@MyApplication, R.color.primary)
            accentColor = ContextCompat.getColor(this@MyApplication, R.color.accent)
            backgroundColor = ContextCompat.getColor(this@MyApplication, R.color.background)
            isDarkTheme = false
        }
    }
}
```

### Required Permissions

Add these permissions to your `AndroidManifest.xml` as needed:

```xml
<!-- Network access -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- File operations -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Optional features -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.VIBRATE" />
```

### ProGuard/R8 Configuration

Add these rules to your `proguard-rules.pro`:

```proguard
# Revaltronics Commons
-keep class com.revaltronics.commons.** { *; }
-dontwarn com.revaltronics.commons.**

# Room Database
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Gson (if using JSON serialization)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
```

## Minimum Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: Latest
- **Java Version**: 17
- **Kotlin**: Latest stable version
- **Gradle**: 7.0+

## Advanced Usage

### Custom Theming

```kotlin
// Apply custom theme
CommonsTheme.apply {
    primaryColor = Color.BLUE
    secondaryColor = Color.CYAN
    surfaceColor = Color.WHITE
    onSurfaceColor = Color.BLACK
}

// Use in Compose
@Composable
fun ThemedContent() {
    CommonsTheme {
        // Your themed content
    }
}
```

### Reactive Programming with RxJava

```kotlin
import com.revaltronics.commons.rx.*

// Observable utilities
Observable.just("Hello")
    .subscribeOnIO()
    .observeOnMain()
    .subscribe { result ->
        // Handle result on main thread
    }

// Disposable management
class MyActivity : BaseSimpleActivity() {
    private val disposables = CompositeDisposable()
    
    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
```

## Troubleshooting

### Common Issues

1. **Build Error: "Failed to resolve dependency"**
   - Ensure JitPack repository is added
   - Check internet connectivity
   - Verify version tag exists

2. **Runtime Error: "Class not found"**
   - Check ProGuard configuration
   - Ensure correct import statements

3. **Theme Issues**
   - Verify Material theme inheritance
   - Check color resource definitions

### Getting Help

- Check [Issues](https://github.com/sunnybhadana/autophone-commons/issues) for known problems
- Create detailed bug reports with reproduction steps
- Include your `build.gradle.kts` when reporting issues

## Examples and Samples

The repository includes a comprehensive samples module demonstrating:

- Basic library usage
- Integration with popular libraries
- Best practices
- Common use cases

To run the samples:

```bash
git clone https://github.com/sunnybhadana/autophone-commons.git
cd autophone-commons
./gradlew :samples:installDebug
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Support

For issues and questions, please open an issue on the [GitHub repository](https://github.com/sunnybhadana/autophone-commons/issues).
