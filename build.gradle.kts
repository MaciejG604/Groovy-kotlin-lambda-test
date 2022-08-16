import com.example.buildsrc.MyExtension

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

allprojects{
    extensions.create("Extension", MyExtension::class.java)

    val addElementNoProjectAllScope by extra { _: Unit ->
        configure<MyExtension> {
            contents.add("addElementNoProjectAllScope")
        }
    }

    //passing a no-argument lambda makes the val EAGERLY initialized with lambda result
    val addElementFakeLambdaAllScope by extra {
        println("Evaluated FakeLambdaAllScope for ${project.name}")
        configure<MyExtension> {
            contents.add("addElementFakeLambdaAllScope")
        }
    }
}

val addElementNoProjectExtScope by extra { _: Unit ->
    configure<MyExtension> {
        contents.add("addElementNoProjectExtScope")
    }
}

//passing a no-argument lambda makes the val EAGERLY initialized with lambda result
val addElementFakeLambdaExtScope by extra {
    println("Evaluated FakeLambdaExtScope for ${project.name}")
    configure<MyExtension> {
        contents.add("addElementFakeLambdaExtScope")
    }
}

val addElementWithProject by extra { project: Project ->
    project.configure<MyExtension> {
        contents.add("addElement")
    }
}

val printContents by extra (fun(project: Project){
    println("${project.name} contents:")
    project.extensions
        .getByType(MyExtension::class)
        .contents.forEach{println(it)}
    println()
})

val getContents by extra (fun(project: Project): Set<String>? {
    return project.extensions
        .getByType(MyExtension::class)
        .contents
})
