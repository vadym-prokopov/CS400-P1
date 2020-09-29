all: compile_all_files
	java BookStoreDriver
	
compile_all_files:
	javac Book.java BookCollection.java BookStore.java BookStoreDriver.java HashTableMap.java
clean:
	$(RM) *.class
	
TestSuite.class: 
	javac -cp .:junit5.jar TestSuite.java
test: TestSuite.class compile_all_files
	java -jar junit5.jar --class-path . --scan-classpath --details tree