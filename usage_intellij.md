# Use in IntelliJ IDEA

* Create a Scala project
* Make sure the FunGraphics library is in your project
    * Download the FunGraphics [jar](https://github.com/hevs-isi/FunGraphics/releases/latest) and place it in project
      directory, at the same level as the `src` directory.
    * Your project directory should look like:
      ```
        LabXX
         ├── FunGraphics-M.m.p.jar
         └── src
             └── LabXX_TaskYY.scala
      ```
* Let _IntelliJ_ know about the library
    * Click on `File` > `Project Structure...`
    * Go in the `Project` > `Librairies` tab
    * Click on the `+` (`New Project Library`) > `Java`
    * Select the FunGraphics jar file inside your project > `OK`
    * `OK` to the message: `"Library FunGraphics-M.m.p.jar will be added to the selected modules"`
    * `OK` to close the `Project Structure...`
* You can test the installation using that simple code which will display an empty window.
  
  ```scala
  import hevs.graphics.FunGraphics

  object FunGraphicsHello extends App {
    val display: FunGraphics = new FunGraphics(300, 400)
  }
  ```
