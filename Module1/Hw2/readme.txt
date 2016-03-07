plugin works in two ways:
1. for the task you configure what file to use and what file to build. Looks like this:
fileMap = [
            "build\\resources\\main\\js\\profile.js": ["build\\resources\\main\\js\\customrange.js", "build\\resources\\main\\js\\memenu.js"],
            "build\\resources\\main\\js\\registration.js": ["build\\resources\\main\\js\\musicShop.js"]
    ]

2. you configure input and output folders and file name for resulting file. Looks like this:
fileName = 'allJs.js'
inputFolder = 'build\\resources\\main\\js'
outputFolder = 'build\\resources\\main\\js'

For TestProject plugin is called in doLast of processResources task.
In the TestProject the first way of working with plugin is used.