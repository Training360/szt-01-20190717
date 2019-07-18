# Employees Java EE alkalmazás

`DataSource` szükséges:

A `jboss-cli.bat` állományt elindítva a parancssorba beírandó:

```
connect
deploy "postgresql-42.2.6.jar"
/subsystem=datasources:installed-drivers-list
data-source add --name=EmployeeDS --jndi-name=java:/jdbc/EmployeeDS \
  --driver-name=postgresql-42.2.6.jar \
  --connection-url=jdbc:postgresql:employees \
  --user-name=employees \
  --password=employees
/subsystem=datasources:read-resource
/subsystem=datasources:read-resource(recursive=true)
```


WildFly-on futtatandó az `wildfly:deploy` parancs kiadásával.