# Java on Github Codespaces

https://docs.github.com/en/codespaces/setting-up-your-project-for-codespaces/setting-up-your-project-for-codespaces?langId=java

Using Java 17: https://github.com/devcontainers/templates/tree/main/src/java

```
$ java --version
openjdk 17.0.5 2022-10-18 LTS
OpenJDK Runtime Environment Microsoft-6841604 (build 17.0.5+8-LTS)
OpenJDK 64-Bit Server VM Microsoft-6841604 (build 17.0.5+8-LTS, mixed mode, sharing)
```

# Java on Mac

Latest OpenJDK.

Was using OpenJDK12 with Hotspot JVM.

https://github.com/AdoptOpenJDK/homebrew-openjdk

```bash
/usr/libexec/java_home -V # mac java installs
jenv add <jdk_path>
jenv global <version> # 1.8.0.202
jenv versions
```

# IDE

IntelliJ Idea community edition latest.

## Java Level setting

- File - Settings - Compiler, Java Compiler
- File - Project Structure - Project Language Level, Module - Source Tab, Language Level
- Maven

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <source>1.8</source>
        <target>1.8</target>
      </configuration>
    </plugin>
  </plugins>
</build>
```

# Maven

```bash
# Check what java version maven is using
$mvn -version
Apache Maven 3.5.4 (1edded0938998edf8bf061f1ceb3cfdeccf443fe; 2018-06-17T11:33:14-07:00)
Maven home: /usr/local/Cellar/maven/3.5.4/libexec
Java version: 1.8.0_202, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.6", arch: "x86_64", family: "mac"
# set maven to use java version set by jenv
$jenv enable-plugin maven
```



## Maven Loading Archetype List in IntelliJ For Ever

[see this link at stack overflow](http://stackoverflow.com/questions/17421103/create-a-maven-project-in-intellij-idea-12-but-alway-in-the-loading-archetype-l)

- update maven repo: Settings->Maven->Repositories - select maven's Central repo (if it's there; otherwise you should add it (http://repo.maven.apache.org/maven2/)) and press Update button.
- Maven->Importing: "VM options for importer" to -Xmx1024m (default is -Xmx512m and it's not enough).
