<a name="readme-top"></a>


<!-- PROJECT LOGO -->
<br />
<div align="center">
 
  <h3 align="center">TEXT EDITOR</h3>
  <p align="center">
    TEXT EDITOR
    <br />
    <a href="https://github.com/DoublEffe/TextEditor"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    ·
    <a href="https://github.com/DoublEffe/TextEditor/issues">Report bug</a>
    ·
    <a href="https://github.com/DoublEffe/TextEditor/issues">Request feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About the Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li>
      <a href="#usage">Usage</a>
    </li>
  </ol>
</details>


## About the Project

This is a text editor that can be used via terminal(only windows).

### Built With

THe project is written in java and maven to build the project.

* [![Java][java badge]][java]
* [![Maven][maven badge]][maven]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

You need only a couple of things to start.

### Prerequisites

* You will need jna-5.16.0.jar (can be found here https://github.com/java-native-access/jna).

## Usage

After compiling you can launch the editor.
  ```sh
    java -cp "path-to-the-snapshot;path-to-jna-jar" org.project.texteditor.TextEditor name-of-file.txt
  ```
You can quit and save to the same file by pressing ctrl + q.
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
[java]: https://www.java.com/  
[java badge]: https://img.shields.io/badge/JAVA%20-DD0031?style=for-the-badge&logo=Java&logoColor=white&logoSize=auto
[maven]: https://maven.apache.org/  
[maven badge]: https://img.shields.io/badge/APACHE%20MAVEN%20-DD0031?style=for-the-badge&logo=ApacheMaven&logoColor=white&logoSize=auto