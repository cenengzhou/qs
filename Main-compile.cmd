@IF NOT DEFINED JAVA_HOME set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_79
@IF NOT DEFINED M2_CACHE set M2_CACHE=%userprofile%\.m2\repository
@set cp=%~dp0src\main\java\;
@set cp=%cp%%~dp0lib\com\gwtext\gwtext\2.0.5\gwtext-2.0.5.jar;
@set cp=%cp%%~dp0lib\com\google\gwt\gwt-widgets\0.2.0\gwt-widgets-0.2.0.jar;
@REM @set cp=%cp%%~dp0lib\com\google\gwt\gwt-math\2.1\gwt-math-2.1.jar;
@set cp=%cp%%M2_CACHE%\com\google\gwt\gwt-dev\2.7.0\gwt-dev-2.7.0.jar;
@set cp=%cp%%M2_CACHE%\com\google\gwt\gwt-user\2.7.0\gwt-user-2.7.0.jar;
@set cp=%cp%%M2_CACHE%\org\ow2\asm\asm\5.0.3\asm-5.0.3.jar;
@set cp=%cp%%M2_CACHE%\org\ow2\asm\asm-util\5.0.3\asm-util-5.0.3.jar;
@set cp=%cp%%M2_CACHE%\org\ow2\asm\asm-tree\5.0.3\asm-tree-5.0.3.jar;
@set cp=%cp%%M2_CACHE%\org\ow2\asm\asm-commons\5.0.3\asm-commons-5.0.3.jar;
@set cp=%cp%%M2_CACHE%\javax\validation\validation-api\1.0.0.GA\validation-api-1.0.0.GA.jar;
@set cp=%cp%%M2_CACHE%\javax\validation\validation-api\1.0.0.GA\validation-api-1.0.0.GA-sources.jar;
@set cp=%cp%%M2_CACHE%\org\hibernate\javax\persistence\hibernate-jpa-2.1-api\1.0.0.Final\hibernate-jpa-2.1-api-1.0.0.Final.jar;
@set cp=%cp%%M2_CACHE%\org\hibernate\javax\persistence\hibernate-jpa-2.1-api\1.0.0.Final\hibernate-jpa-2.1-api-1.0.0.Final-sources.jar;
@set cp=%cp%%M2_CACHE%\org\hibernate\hibernate-core\4.3.11.Final\hibernate-core-4.3.11.Final.jar;
@set cp=%cp%%M2_CACHE%\org\hibernate\hibernate-core\4.3.11.Finalhibernate-core-4.3.11.Final-sources.jar;
@set cp=%cp%%~dp0target\classes\;
"%JAVA_HOME%\bin\java" -Xmx768M -cp "%cp%" com.google.gwt.dev.Compiler -sourceLevel 1.7 -war "%~dp0src\main\webapp" %* com.gammon.qs.GammonQS 
@REM -style pretty -logLevel DEBUG > gwt.log 