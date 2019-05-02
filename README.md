# TextDragger
[![](https://jitpack.io/v/GingGingI/Textdragger.svg)](https://jitpack.io/#GingGingI/Textdragger)

![](https://media.giphy.com/media/Swlx3kICIORyxGOA3t/giphy.gif)
===========
### SetUp

Step 1. JitPack을 빌드단위 gradle에 추가 (Add it in your root build.gradle at the end of repositories:)

gradle

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. dependency 에 추가 (Add the dependency)

	dependencies {
	        implementation 'com.github.GingGingI:Textdragger:0.1'
	}

How To?

layout.xml

         <c.gingdev.draggabletext.draggableTextView
            android:id="@+id/draggableText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

activity.kt

        draggableText.attach(viewPager)
        draggableText.addText(firstText)
        draggableText.addText(secondText)
