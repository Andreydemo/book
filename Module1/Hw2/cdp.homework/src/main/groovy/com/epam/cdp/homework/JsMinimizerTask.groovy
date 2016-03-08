package com.epam.cdp.homework

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JsMinimizerTask extends DefaultTask {
    public static final String BACK_SLASH = "\\"
    public static final String WHITESPACE = " "
    public static final String DOUBLE_QUOTE = "\""
    public static final String QUOTE = "'"
    public static final String SLASH = "/"
    public static final String STAR_SLASH = "*/"
    public static final String NEW_LINE = "\n"

    Map<String, List<String>> fileMap;
    String fileName
    String inputFolder
    String outputFolder

    StringBuffer buffer
    StringBuffer result

    private static
    final String KEYWORD_REGEXP = "instanceof|in|typeof|var|return|throw|new|function(?=\\s+\\w+)|else(?=\\s+if)"
    private static final String REGEXP_BEFORE_JS_REGEXP = "(?s).*[=(\\[]\\s*/";

    @TaskAction
    public void minimize() {
        if (fileMap == null) {
            JsFile resultFile = new JsFile(outputFolder + BACK_SLASH + fileName);
            result = new StringBuffer()
            new File(inputFolder).eachFileMatch(~/.*.js/) { file ->
                JsFile jsFile = new JsFile(file)
                if (!jsFile.file.name.equals(fileName)) {
                    minimizeFile(file)
                    jsFile + result.toString()
                    file.delete()
                }
            }
        } else {
            fileMap.each { entry ->
                JsFile resultFile = new JsFile(entry.key);
                result = new StringBuffer()
                def list = entry.value
                list.each {
                    def file = new File(it)
                    JsFile jsFile = new JsFile(file)
                    minimizeFile(file)
                    resultFile + result.toString()
                    file.delete()
                }
            }
        }
    }

    public void minimizeFile(def file) {
        buffer = new StringBuffer(file.text)
        while (buffer.length() != 0) {
            def map = ["\"": buffer.indexOf(DOUBLE_QUOTE), "'": buffer.indexOf(QUOTE), "/": buffer.indexOf(SLASH)]
            while (!map.isEmpty() && map.min { it.value }.value == -1) {
                map.remove(map.min { it.value }.key)
                continue
            }
            if (map.isEmpty()) {
                removeWhitespaces(buffer.subSequence(0, buffer.length()))
                buffer.delete(0, buffer.length())
            } else {
                def minEntry = map.min { it.value }
                if (minEntry.key == SLASH)
                    processSlash(minEntry.key, minEntry.value)
                else {
                    removeWhitespaces(buffer.subSequence(0, minEntry.value))
                    minEntry = map.min { it.value }
                    processUnmodifiableBlock(minEntry.key, minEntry.value)
                }
            }
        }
    }

    void processUnmodifiableBlock(String string, int index) {
        boolean flag = true;
        int nextIndex = buffer.indexOf(string, index + 1)
        while (flag) {
            if (nextIndex != -1) {
                if (buffer.charAt(nextIndex - 1) == '\\') {
                    nextIndex = buffer.indexOf(string, nextIndex + 1)
                    continue
                }
                result.append(buffer.subSequence(index, nextIndex + 1))
                buffer.delete(0, nextIndex + 1)
            } else {
                result.append(string)
                buffer.delete(0, buffer.length())
            }
            flag = false
        }
    }

    void removeWhitespaces(String string) {
        string = string.replaceAll(NEW_LINE, WHITESPACE)
        string = string.replaceAll("\\s+", WHITESPACE)
        string = string.replaceAll("(?<!$KEYWORD_REGEXP)\\s+(?!$KEYWORD_REGEXP)", "")
        string = string.replaceAll("(?<!\\w)\\s(?=typeof|var|return|throw|new|function)", "")
        result.append(string)
    }

    void processSlash(String string, int index) {
        if (buffer.charAt(index + 1) == '*') {
            int endOfCommentIndex = buffer.indexOf(STAR_SLASH);
            if (endOfCommentIndex == -1) buffer.delete(index, buffer.length())
            else buffer.delete(index, endOfCommentIndex + 2);
        } else {
            int nextIndex = buffer.indexOf(SLASH, index + 1)
            if (nextIndex == index + 1) {
                int newLineIndex = buffer.indexOf(NEW_LINE, index + 1)
                if (newLineIndex != -1)
                    buffer.delete(index, newLineIndex)
                else
                    buffer.delete(index, buffer.length())
            } else {
                if (buffer.subSequence(0, index + 1).matches(REGEXP_BEFORE_JS_REGEXP)) {
                    removeWhitespaces(buffer.subSequence(0, index))
                    processUnmodifiableBlock(string, index)
                } else {
                    removeWhitespaces(buffer.subSequence(0, index + 1))
                    buffer.delete(0, index + 1)
                }
            }
        }
    }
}