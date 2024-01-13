package hevs.utils

import java.io.{File, IOException}
import java.util
import java.util.regex.Pattern
import java.util.zip.{ZipEntry, ZipException, ZipFile}

/**
 * List resources available from the classpath
 *
 * Licence : CC BY-SA 4.0
 * @author <a href='https://stackoverflow.com/users/260990/jmj'>jmj</a>
 * @see [[https://stackoverflow.com/a/3923182/2069348]]
 */
object ResourceList {
  /**
   * Gets all resources matching the specified pattern
   * in the classpath
   * To get all resources, pass the pattern ".*":
   * {{{Pattern pattern = Pattern.compile(".*")}}}
   *
   * @param pattern the pattern to match
   * @return the resources in the order they are found
   */
  def getResources(pattern: Pattern): util.Collection[String] = {
    val retval = new util.ArrayList[String]
    val classPath = System.getProperty("java.class.path", ".")
    val classPathElements = classPath.split(System.getProperty("path.separator"))
    for (element <- classPathElements) {
      retval.addAll(getResources(element, pattern))
    }
    retval
  }

  /**
   * Gets all resources matching the specified pattern
   * in the given path element
   * @param element the path element (a directory or .jar file)
   * @param pattern the pattern to match
   * @return the resources in the order they are found
   */
  private def getResources(element: String, pattern: Pattern): util.Collection[String] = {
    val retval = new util.ArrayList[String]
    val file = new File(element)
    if (file.isDirectory) retval.addAll(getResourcesFromDirectory(file, pattern))
    else retval.addAll(getResourcesFromJarFile(file, pattern))
    retval
  }

  /**
   * Gets all resources matching the specified pattern
   * in the given .jar file
   * @param file the .jar file
   * @param pattern the pattern to match
   * @return the resources in the order they are found
   */
  private def getResourcesFromJarFile(file: File, pattern: Pattern): util.Collection[String] = {
    val retval = new util.ArrayList[String]
    var zf: ZipFile = null
    try zf = new ZipFile(file)
    catch {
      case e: ZipException =>
        throw new Error(e)
      case e: IOException =>
        throw new Error(e)
    }
    val e = zf.entries
    while ( {
      e.hasMoreElements
    }) {
      val ze = e.nextElement.asInstanceOf[ZipEntry]
      val fileName = ze.getName
      val accept = pattern.matcher(fileName).matches
      if (accept) retval.add(fileName)
    }
    try zf.close()
    catch {
      case e1: IOException =>
        throw new Error(e1)
    }
    retval
  }

  /**
   * Gets all resources matching the specified pattern
   * in the given directory
   * @param directory the directory to search in
   * @param pattern the pattern to match
   * @return the resources in the order they are found
   */
  private def getResourcesFromDirectory(directory: File, pattern: Pattern): util.ArrayList[String] = {
    val retval = new util.ArrayList[String]
    val fileList = directory.listFiles
    for (file <- fileList) {
      if (file.isDirectory) retval.addAll(getResourcesFromDirectory(file, pattern))
      else try {
        val fileName = file.getCanonicalPath
        val accept = pattern.matcher(fileName).matches
        if (accept) retval.add(fileName)
      } catch {
        case e: IOException =>
          throw new Error(e)
      }
    }
    retval
  }

  /**
   * Lists the resources that match `args(0)`
   *
   * @param args
   * args(0) is the pattern to match, or list all resources if
   * there are no args
   */
  def main(args: Array[String]): Unit = {
    var pattern: Pattern = null
    if (args.length < 1) pattern = Pattern.compile(".*")
    else pattern = Pattern.compile(args(0))
    val list = ResourceList.getResources(pattern)
    list.forEach(println(_))
  }
}