# Publish Date #

2011/07/31

# Introduction #

It's a newer version for Run Jetty Run again,
refine the run configuration and new features/bug fixs for RJR.


# Update Site #

http://run-jetty-run.googlecode.com/svn/trunk/updatesite


# Details #

  * Issue list http://code.google.com/p/run-jetty-run/issues/list?can=1&q=label%3Afix-1.3

  * What's new in 1.3
    * features
      * [Issue 55](https://code.google.com/p/run-jetty-run/issues/detail?id=55) : Jetty7/Jetty8 support
      * [Issue 54](https://code.google.com/p/run-jetty-run/issues/detail?id=54) : ignore WTP runtime jars when user using server runtime lib.
      * [Issue 81](https://code.google.com/p/run-jetty-run/issues/detail?id=81) :M2E issue with web container classpath
      * [Issue 76](https://code.google.com/p/run-jetty-run/issues/detail?id=76) :add a option to let source scanner scan WEB-INF folder
      * [Issue 72](https://code.google.com/p/run-jetty-run/issues/detail?id=72) :if eclipse crash, auto terminate RJR
      * [Issue 68](https://code.google.com/p/run-jetty-run/issues/detail?id=68) :add a option to exclude classpath entry.
      * [Issue 66](https://code.google.com/p/run-jetty-run/issues/detail?id=66) :write a preference to config default enable source scanner or not.
      * [Issue 65](https://code.google.com/p/run-jetty-run/issues/detail?id=65) :provide a option to automatically find a unused port when creating a run configuration
      * [Issue 64](https://code.google.com/p/run-jetty-run/issues/detail?id=64) :shutdown issue
      * [Issue 62](https://code.google.com/p/run-jetty-run/issues/detail?id=62) :change default MexPermSize to 128m
    * Fixed bugs
      * [Issue 75](https://code.google.com/p/run-jetty-run/issues/detail?id=75) [nightly](nightly.md)source scanner not working in Jetty7
      * [Issue 63](https://code.google.com/p/run-jetty-run/issues/detail?id=63) port detect not working when you launch multiple times very fast
      * [Issue 67](https://code.google.com/p/run-jetty-run/issues/detail?id=67) detect if webapp exist when launching
      * [Issue 70](https://code.google.com/p/run-jetty-run/issues/detail?id=70) Run jety run can't launch if project name   contains space
      * [Issue 79](https://code.google.com/p/run-jetty-run/issues/detail?id=79) shortcut bug
      * [Issue 78](https://code.google.com/p/run-jetty-run/issues/detail?id=78) RJR failing to include required jars in classpath


# Notice #

  * Feel free to report anything you find , no matter bug or opinion. :p

  * To speed up your installation ,please don't check this option.
> > http://screencast.com/t/DuCNNp4D59R

  * For Jetty8 's restriction , you have to use a "JDK Runtime" instead of default JRE Runtime to run it, or the JSP complier will not work.

  * Currently we do not support the Servlet3 annotations in Jetty8 ,    we will try to handle this in 1.3.1 soon.



# Next Milestone #

Here we list the defects we are going to do in version 1.3.1 ;

  * [Issue 85](https://code.google.com/p/run-jetty-run/issues/detail?id=85)  Jetty8 with servlet3 annotation support


And thanks James Synge , Eelco Hillenius for building a such good project and invited me to join the project.

I wish I could make it better.