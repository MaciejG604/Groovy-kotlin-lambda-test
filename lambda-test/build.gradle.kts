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

val printContents: (Project) -> Unit by rootProject.extra
val getContents: (Project) -> Set<String> by rootProject.extra

val addElementNoProjectExtScope: (Unit) -> Unit by rootProject.extra
val addElementNoProjectAllScope: (Unit) -> Unit by extra
val addElementWithProject: (Project) -> Unit by rootProject.extra

//Fake lambda in ext scope will run its code only once and capture project from the scope where it was defined (here root build script)
assert(getContents(project) == listOf("addElementFakeLambdaExtScope").toSet())
//Fake lambda in allprojects scope will run its code only once and capture project from surrounding scope (from surrounding script)
assert(getContents(rootProject) == listOf("addElementFakeLambdaExtScope", "addElementFakeLambdaAllScope").toSet())

println("Sizes: project=1 rootProject=2")
printContents(project)
printContents(rootProject)

//Lambda with no usage of project parameter in the ext scope will capture project from the scope where it was defined (here root build script)
addElementNoProjectExtScope(Unit)
println("Sizes: project=1 rootProject=3")
printContents(project)
printContents(rootProject)
assert(getContents(project).size == 1)
assert(getContents(rootProject).size == 3)


//Lambda with no usage of project parameter but in the allprojects scope will capture project from surrounding scope (from surrounding script)
addElementNoProjectAllScope(Unit)
println("Sizes: project=2 rootProject=3")
printContents(project)
printContents(rootProject)
assert(getContents(project).size == 2)
assert(getContents(rootProject).size == 3)

//Lambda with project argument configures elements in a specified project only
addElementWithProject(project)
println("Sizes: project=3 rootProject=3")
printContents(project)
printContents(rootProject)
assert(getContents(project).size == 3)
assert(getContents(rootProject).size == 3)

//Lambda with project argument configures elements in a specified project only
addElementWithProject(rootProject)
println("Sizes: project=3 rootProject=4")
printContents(project)
printContents(rootProject)
assert(getContents(project).size == 3)
assert(getContents(rootProject).size == 4)
