# Couchbase Shell

The Couchbase Shell is a standalone toolkit that can be used to work with your Couchbase Server cluster in a easy
and straightforward way.

## Basic Usage
The easiest way is to [download the distribution](http://dl.bintray.com/daschl/generic/couchbase-shell-0.1.zip) and
unpack it. You'll find executable files for both unix and windows systems in the `bin` directory. You will be greeted
with a nice banner:

```
┌─[michael@daschlbase]─[~/Desktop/couchbase-shell]
└──╼ bin/couchbase-shell
.--.             .    .                      .-. .        . .
:                |    |                     (   )|        | |
|    .-. .  . .-.|--. |.-.  .-.  .--. .-.    `-. |--. .-. | |
:   (   )|  |(   |  | |   )(   ) `--.(.-'   (   )|  |(.-' | |
`--'`-' `--`-`-''  `-'`-'  `-'`-`--' `--'   `-' '  `-`--'`-`-
Version:1.0.0-SNAPSHOT
Welcome to the interactive Couchbase Shell!
cb-shell>
```

You can use the `help` command to show all commands and also `elp <command>` to get more infos on a specific command
and its options.

### Connecting and Disconnecting
If you just type `connect`, it assumes a Couchbase Server installation running on localhost. You can customize the,
`hostname`, `bucket` and `password` through their appropriate flags:

```
cb-shell>connect
Connected.
cb-shell[default]>disconnect
Disconnected.
```

```
cb-shell>connect --hostname "127.0.0.1" --bucket "default"
Connected.
```

You can only be connected to one bucket at a time. If you try to connect twice before disconnecting, you'll get a
message like: `Command 'connect' was found but is not currently available (type 'help' then ENTER to learn about
this command)`. You will also see a "disabled" command if you try to do operations against a bucket but you are not
connected currently. So remember to connect first!

### Basic Operations
Let's try to load a document by its key:

```
cb-shell[default]>get mykey
Success: false, Message: Not found
```

Looks like the key does not exist, so let's create a document and then try to load it again (add and replace work
similarly):

```
cb-shell[default]>set mykey --value "content"
Success: true, Message: OK
cb-shell[default]>get mykey
Success: true, Message: OK
content
```

We can also delete it:

```
cb-shell[default]>delete mykey
Success: true, Message: OK
cb-shell[default]>get mykey
Success: false, Message: Not found
```

## Contributing
Please file bugs and enhancements in the GitHub bugtracker. The project itself is java-based and uses gradle as the
underlying dependency and build management framework. I recommend you to check out IntelliJ 13 which has excellent
gradle support, importing it works like a breeze.