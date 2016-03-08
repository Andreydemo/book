package com.epam.cdp.homework

class JsFile {
    File file

    JsFile(String path) {
        file = new File(path);
    }

    JsFile(File file) {
        this.file = file
    }

    JsFile plus(JsFile anotherFile) {
        file.write(anotherFile.file.text)
    }

    JsFile plus(String string) {
        file.write(string)
    }
}
