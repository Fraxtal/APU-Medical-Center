# APU Medical Center

  An application that streamline the appointment booking and payment process by connecting different types of users such as managers, staff, doctors, and customers.

  ---

## 📂 Project Structure

```dir
    /project-root
    ├── build.xml # Ant build script
    ├── src/ # Java source files
    │ └── startup.java (main entry point)
    ├── lib/ # External libraries
    └── dist/ # Compiled .class or .jar files (after build)
```

---

## ⚙️ Requirements
- Java 24 
- Apache Ant  

---

## 🏗️ Build Instructions
To compile the project, run the following in the project root:  

```bash
ant compile
```

To clean previous builds:

```bash
ant clean
```

---

## ▶️ Running the Application

If running directly from Ant:

```bash
ant run
```

If running from the compiled JAR (after ant jar):

```bash
java -cp dist/assignment.jar startup
```

## 📦 Dependencies

* commons-logging-1.3.5-jar

* fontbox-3.0.5.jar

* pdfbox-3.0.5.jar

* jcalendar-1.4.jar

---

## 👥 Contributors

This project was developed through collaboration by the following team members:

[Nicholas Pang Tze Shen](https://github.com/Fraxtal) – Project Lead, Customer and User Role Features, File Management, Debugging

[Ng Wei Hao](https://github.com/02-is-02) – Ant Build Setup, Manager Role Features, Database Integration, File Management, Debugging

[Sean Ng Yi Da](https://github.com/SeanEYD) – Staff role features, Testing, Documentation

[Teoh Kai Chen](https://github.com/KingstonTeoh) – Doctor role features, Testing, Documentation

---

## 📝 Notes

* The program starts execution in startup.java.


* If you modify the source code, always re-run ant compile before executing.
