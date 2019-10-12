go1:
	export CLASSPATH=.:/	
	@javac *.java
	@java data_processing lbnl.anon-ftp.03-01-11.csv
	@rm *.class
go2:
	export CLASSPATH=.:/	
	@javac *.java
	@java data_processing lbnl.anon-ftp.03-01-14.csv
	@rm *.class
go3:
	export CLASSPATH=.:/	
	@javac *.java
	@java data_processing lbnl.anon-ftp.03-01-18.csv
	@rm *.class
