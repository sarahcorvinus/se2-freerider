# script to set environment variables
# run with: source .env.sh
#
isWindows=$(uname | grep 'CYGWIN\|MINGW')
[ "$isWindows" ] && sep=';' || sep=':'

# build CLASSPATH environment variable using: mvn dependency:build-classpath
export CLASSPATH="target/classes"
export CLASSPATH="${CLASSPATH}${sep}$(mvn dependency:build-classpath | grep repository)"

# function to print classpath with separated lines
function classpath() {
  echo "CLASSPATH=${CLASSPATH}" | tr "${sep}" "\n"
}

# call function and show classpath with lines separated
classpath
