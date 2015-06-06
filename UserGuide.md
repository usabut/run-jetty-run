# Introduction #

Here we explain most the information Run-Jetty-Run user might need to know.


---


# How to use it in easiest way #

Right click your project in project explorer and select "run-as" => "Run Jetty" ,it's really easy to setup and run it , check [GettingStarted](GettingStarted.md) out!

Note: the only requirement is that your project have to contain a "WEB-INF" folder.


---


# Hot key #

**Alt + Z**

There's a hot key to launch the server just like launching java applications,
you could customize it "Debug Jetty Web Application" in Eclipse Preference "General > Keys" page.


**Type "q" in the console and hit Enter**

To cause a graceful Jetty shutdown.


**Type "r" in the console and hit Enter**

To redeploy the application in the running container.


---


# How to select Jetty version (Jetty 6/7/8) #

Default it's Jetty 6.1.26 , if you install "Jetty7 Support" or "Jetty8 Support" feature (which could be found in update-site) ,

you could open the run configuration to find the project,
and there's a select menu to select the version.

<a href='http://www.youtube.com/watch?feature=player_embedded&v=5PWc5HUlglw' target='_blank'><img src='http://img.youtube.com/vi/5PWc5HUlglw/0.jpg' width='425' height=344 /></a>




---


# How to change the port / context path / web-app directory #

![http://files.tonyq.org/eclipse/runjettyrun/rjr133_runconfiguration1.png](http://files.tonyq.org/eclipse/runjettyrun/rjr133_runconfiguration1.png)

You could change the port , context-path, web-app directory in the run configuration.

  * If you want to use root context , please enter "/" in the context-path.

  * The scan button will find the "WEB-INF" in the project and use its parent as web-app.


---


# How to work with Java hot-deploy #

  * You don't really have to restart the server after you change java code,launch RJR with Debug Mode (debug-as -> Run Jetty) and you are now able to enjoy the default hot-deploy feature from JDK. ;)

  * For smart users,I strongly recommend to use [DCEVM](http://ssw.jku.at/dcevm/) to experience best hot-deploy!
    * By default the java hot-deploy didn't allow you to change method signature and member field , or even class name.
    * **You are now able to change anything when you use [DCEVM](http://ssw.jku.at/dcevm/).** (exexcpeting static member , you could change it without restart but it won't apply immediately .)
    * DCEVM only support JDK 1.6 and later.
    * It speed up my development a lot ,just download and one more click , it takes you 5 minute to install it but saving tens of minutes daily !


---


# What's the difference between Jetty Classpath and Webapp Classpath #

They are not the same , in most case you will only access the webapp classpath , the Jetty classpath will only be changed when you want to extends Jetty or change jetty version.

For more details , please reference to [Classpath](Classpath.md) .


---


# How to update web-app classpath to add/exclude jars #

Please reference to [Classpath](Classpath.md) .


---


# What's Source Monitor #

  * It's also the source scanner since RJR 1.1 ,**which will help you restart the server when source code is changed.**

  * Very useful when you are developing with some framework which require a lof of configuation and restart the server.(Spring/Hibernate...etc)

  * Now default disable after 1.3.3 , you could enable it manually in the run configuration.

For more details about full details of source monitor, please reference to [SourceMonitorFeature](SourceMonitorFeature.md) page.



---



# How to use JNDI ? #

Since JNDI is not a common case for J2EE development  ,
so we didn't load JNDI classpath by default.

You could enable the "JNDI support" in "Advanced options" of Run Configuraiton.

![http://files.tonyq.org/eclipse/runjettyrun/rjr133_runconfiguration2.png](http://files.tonyq.org/eclipse/runjettyrun/rjr133_runconfiguration2.png)




---



# How to enable SSL #

Please reference to [SSLGuide](SSLGuide.md) page for more details.


---


# Other advanced usage or system properties we supprot #

Please reference to [Advanced Usage](AdvancedUsage.md) to see more thing we could do.