# Find Performance Bottlenecks by Profiling Integration Tests in CI
dotTrace Profiler is a plugin to JetBrains Teamcity 9.0 and later that allows you to perform performance profiling as one of the continuous integration steps.

All you need is to:
* Write a number of integration tests that cover performance-critical functionality of your app.
* In the plugin parameters, set performance thresholds for these tests (or any of the underlying methods). The threshold can be set as an absolute number in ms for method's own or total (own + call subtree) time. Another option is to check method's execution time against previous successful builds.
* That's it! Once the build is run, the plugin runs the tests and checks the execution time of the specified methods. If any of the thresholds are exceeded, the build is considered failed. After running the build step, dotTrace Profiler plugin saves a performance snapshot. Analyze it in the standalone dotTrace profiler and find out the cause of performance issues.

## How to Build
1. Issue the 'mvn package' command from the root project.
The resulting package 'dotTracePlugin.zip' will be placed to the 'target' directory.
Note that to build the plugin, Apache Maven must be installed on your machine.

## How to Install
1. Copy the zip from the 'target' directory to 'plugins' under the TeamCity data directory.
2. Restart the 'TeamCity Server' service.
3. Copy the dotTrace console profiling tool to a desired TeamCity agent.
*IMPORTANT! To provide profiling results consistency, you should pin the build configuration that uses the plugin to a specific build agent.*

## How to Use
1. Write integration tests that cover certain application functionality.
2. dotTrace Profiler plugin uses the dotTrace console tool for profiling. Therefore, to run the build step, you should create a profiling configuration for this tool. It is strongly recommended that you use the 'Configuration2Xml' tool to create the configuration. For more details, refer to https://www.jetbrains.com/profiler/help/Performance_Profiling__Profiling_Using_the_Command_Line.html
*IMPORTANT! When specifying the profiling target in the configuration, use the %CHECKOUTDIR% placeholder. The plugin
will automatically replace it with the path to the project checkout directory on the agent.*
3. In your build configuration, add the 'dotTrace Profiler' build step.
4. Specify the following options for the step:
  * **dotTrace path** - path to the dotTrace console tool directory on the agent.
  * **Profiling config file path** - path to the profiling configuration file you've created on step 2.
  * **Temp directory path** - path to the temporary directory used to store performance snapshot collected during the build step. Note that snapshot files are re-created each time the build is run.
  * **Publish performance snapshot to artifacts** - specify when the plugin should save the performance snapshot: never, always, or only when the performance thresholds are exceeded.
  * **Threshold values** - specify the newline-separated list of methods which performance you want to check. These can
 be particular integration tests as well as any of the methods run by these tests. Use the built-in help for more details.
5. Save the build configuration and run the build. If at least one of the profiled methods doesn't meet its threshold, the build is considered failed. 
6. To view profiling results, open the **Build Log** for the build. Note that profiling results (execution times of specified methods) are also reported as build statistic values. Thus, you can view their graphs on **Parameters | Reported statistic values** page of the current build.
