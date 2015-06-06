## Run Jetty Run ##

  * Run web applications with Jetty and Eclipse in one click!

  * Full maven support , simply run maven J2EE project without any config.

  * GettingStarted explains it all.  Please see FutureDirectionsDiscussion to participate in the evolution of Run Jetty Run.

## Update Site ##

  * http://run-jetty-run.googlecode.com/svn/trunk/updatesite

## News ##
  * We have recently been working on jetty version update for Jetty7/Jetty8 and Jetty 9 support . For more details , please reference to our [[Nightly](Nightly.md)] page.

  * 1.3.3 is released ,some minor bug fixing and move to latest jetty version!

  * **Strongly recommand to use [DCEVM](http://ssw.jku.at/dcevm/) hot-deploy feature to work with RJR, it's really faster and faster.Try it , you will love it! ;)**

  * If you have any questions ,feel free to post your questions on our facebook fans page.
> > http://www.facebook.com/RunJettyRun

  * We have clean some old version to prevent confusion ,
> > if you need older version (before 1.3.2 ) , please use the following update-site.
> > http://run-jetty-run.googlecode.com/svn/trunk/updatesite-old

  * Now Run Jetty Run is available on Marketplace!
    * http://marketplace.eclipse.org/content/run-jetty-run

  * Please feel free to leave your comment to us here! [UsersFeelingAboutRJR](UsersFeelingAboutRJR.md)

  * Nightly Update Site http://run-jetty-run.googlecode.com/svn/trunk/updatesite-nightly
    * For more details please reference to [Nightly feature and issues ](Nightly.md)


---

## Why this plugin? ##

Because running a web application in Eclipse should be as simple as 'clicking run'. No additional setup required.


## The difference with WTP Jetty Integration ##

I(Tony) think there are some benefits to use Run-Jetty-Run instead of WTP:

  * Performance
> > WTP always copy all the resources to a temp folder , that slow down the process and  you have to stop server then clean the resource sometimes if the resource locked. WTP solution also takes more time to start and terminate a web application.

  * The maven support
> > It's a annoying to run maven based web application in WTP. Since we use same project classpath with default Eclipse JDT , so we could support maven dependency management (M2Eclipse Plug-in) easily. (Note that RunJettyRun did not required to install a maven plug-in , it's optional.)

  * Less steps to setup and run
> > For using WTP Jetty ,  you have to install Jetty\_WTP\_Plugin , then find a runtime for it , that took your time to download a runtime and config it. You still have 3-4 steps after you install WTP plugin.But if you use RunJettyRun , there's already a built-in Jetty lib in the plug-in , simply "run" a project with Jetty by one click.


> <a href='http://www.youtube.com/watch?feature=player_embedded&v=Dtj1YBy9LKw' target='_blank'><img src='http://img.youtube.com/vi/Dtj1YBy9LKw/0.jpg' width='425' height=344 /></a>

  * Also support Java project.
> > Easy to use Jetty for every Java project but also a Dynamic Web Project , if you are writing some web widget , it's easy to test it with RJR.


## Other benefits ##

  * Working with Eclipse JDT debugging/ hot deploy
> > When you running RJR in debug mode , you could modify the code and apply it directly. Of course, all the breakpoint/inspector for java resource is working fine.



---

## Change log ##

### 1.3.3 ###
_Created by Tony Wang  2012/3/16_


> [Run Jetty Run 1.3.3 release note](ReleaseNote133.md)

### 1.3.2 ###
_Created by Tony Wang  2012/1/6_

> [Run Jetty Run 1.3.2 release note](ReleaseNote132.md)

### 1.3.1 ###
_Created by Tony Wang  2011/9/14_

> Update Jetty7/Jetty8 embed Jetty version and add servlet3 annotation support for Jetty8 ([Issue 85](https://code.google.com/p/run-jetty-run/issues/detail?id=85)).

### 1.3.0 ###
_Created by Tony Wang  2011/7/31_

> [Run Jetty Run 1.3 release note](ReleaseNote13.md)

### 1.2.2.1 ###
_Created by Tony Wang  2011/5/19_

> Fix path issue for Ubuntu user.

### 1.2.2 ###
_Created by Tony Wang  2011/5/19_

> [Run Jetty Run 1.2.2 release note](ReleaseNote122.md)

> Simple introduce for current RunJettyRun and new feature in 1.2.2
> <a href='http://www.youtube.com/watch?feature=player_embedded&v=fd8iHPEoNec' target='_blank'><img src='http://img.youtube.com/vi/fd8iHPEoNec/0.jpg' width='425' height=344 /></a>



### 1.2.1.1 ###
_Created by Tony Wang  2011/3/24_

> (Fix for Eclipse 3.5 support.)

### 1.2.1 ###
_Created by Tony Wang  2011/3/22_

> [Run Jetty Run 1.2.1 release note](ReleaseNote121.md)



### 1.2 ###
_Created by Tony Wang  2011/3/11_

> [Run Jetty Run 1.2 release note](ReleaseNote12.md)

### 1.1.1 ###
_Created by James Synge  2009/5/7_

> [Jetty and RJR 1.1.0 don't use the same path separator on Unix](http://code.google.com/p/run-jetty-run/issues/detail?id=25)

### 1.1.0 ###
_Created by James Synge  2009/5/4_

> [Add referenced projects to class path](http://code.google.com/p/run-jetty-run/issues/detail?id=8)

> [SSL Support](http://code.google.com/p/run-jetty-run/issues/detail?id=12)

### 1.0.1 ###
_Created by Eelco Hillenius  2008/2/3_

_I'm sorry but I think I messed up the first build; Eclipse won't find updates on the 1.0.0. You'll have to manually de-install 1.0.0 and install 1.0.1_.

> [Added support for JSPs](http://code.google.com/p/run-jetty-run/issues/detail?id=1)

> [Added servlet dependency](http://code.google.com/p/run-jetty-run/issues/detail?id=3)

> [Fixed dependency issue that prevented the plugin from working in Eclipse 2.2](http://code.google.com/p/run-jetty-run/issues/detail?id=5)



### 1.0.0 ###
_Created by Eelco Hillenius 2008/2/3_

> First version