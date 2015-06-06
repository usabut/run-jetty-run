**This document is a bit overdue for latest version , we will update this document soon.**

**The scanner configurations is now moved to Source monitor tab of RunConfiguration after 1.3.2**

# Introduction #

RunJettyRun Configurations for 1.3


# Run Configuration #

## Screenshot ##

![http://files.tonyq.org/eclipse/runjettyrun/RunJettyRun13.png](http://files.tonyq.org/eclipse/runjettyrun/RunJettyRun13.png)

## Details ##

  1. Jetty Version , if you install Jetty7/Jetty8 support , here you could select the version you like . Default is Jetty6. (#1)
  1. Project , usually the selected one. It's seldom to changed.
  1. The port , which should not conflict with other existing service or web container , by default is 8080.
  1. context path, which will be part of your URL, it should start with "/".
  1. Web application directory ,system will look out the one contains WEB-INF/web.xml , so usually we seldom change it.
  1. Scan - A button to help you search the folder that contains "WEB-INF/web.xml".
  1. Only visible when you have M2Eclipse installed, it will exclude all the classpath that endwith test-classes folder.A specific feature for maven project. (Reference to [Issue 42](https://code.google.com/p/run-jetty-run/issues/detail?id=42) )
  1. Advanced Options : Kind of expert mode.
  1. SSL support , check and enter all the data you need , since it's seldom used feature ,it's disabled by default.
  1. Scanner: Source scanner will scan all the source folder (but not the WEB-INF) , and restart the server when you updated the source code or some resource in the folder , like hibernate.cfg.xml ...etc
  1. Scanner Interval: the scanner period , default is 5 seconds.
  1. Scan web-inf folder , it's useful for you need to change web.xml or applicationContext.xml (spring) often and want to restart automatically, it is not enabled by default.
  1. parentLoaderPriority , a Jetty configuration , my suggestion is better to enable it . For more details , reference to [Jetty Classloading](http://docs.codehaus.org/display/JETTY/Classloading).
  1. JNDI support : if you need JNDI , just enable it.
  1. A chance to exclude some classpath you don't like , it will use this to match each classpath  one by one , if the classpath matched ,RJR won't load it . (The matched result will show up on the console.)



_#1 If you select Jetty8 and need to work with JSP (most of projects need JSP support) , you have to configuration the Project JRE for a JDK version.  That's the restriction of Jetty8._