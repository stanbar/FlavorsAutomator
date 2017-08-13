import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Stream

fun main(args: Array<String>) {

    val path = "/Users/admin1/GoogleDrive/AndroidStudioProjects/WallpapersOneClick/app/src"

    //printFlavors(Files.list(Paths.get(path)),gradleFlavorFormatter)
    printFlavors(Files.list(Paths.get(path)), fastlaneFlavorFormatter)
    printSummary(Files.list(Paths.get(path)))
}


val gradleFlavorFormatter = { flavorName: String ->
    "$flavorName {\n" +
            "            applicationIdSuffix \".$flavorName\"\n" +
            "            versionNameSuffix \"-$flavorName\"\n" +
            "\n" +
            "        }"
}
val fastlaneFlavorFormatter = { flavorName: String ->
    "  \n" +
            "  lane :$flavorName do\n" +
            "    gradle(task: \"assemble\", flavor: \"$flavorName\", build_type: \"Release\")\n" +
            "  end"
}

fun printFlavors(files: Stream<Path>, formatter: (String) -> String) {
    for (file in files)
        if (isValidFlavor(file.fileName.toString()))
            println(formatter(file.fileName.toString()))
}

fun printSummary(files: Stream<Path>) {
    val flavors = ArrayList<String>()
    for (file in files)
        if (isValidFlavor(file.fileName.toString()))
            flavors.add(file.fileName.toString())
    println(fastlaneSummarizerFormatter(flavors))

}

val excluded: List<String> = Arrays.asList("main", "test", "androidTest", ".DS_Store")
fun isValidFlavor(fileName: String): Boolean {
    return fileName !in excluded
}

val fastlaneSummarizerFormatter = { flavors: ArrayList<String> ->
    val builder = StringBuilder()
    builder.append("  desc \"Assemble All flavors\"\n")
    builder.append("  lane :assembleAll do\n")
    for (flavor in flavors)
        builder.append("     $flavor\n")
    builder.append("  end")
}


