#!/usr/bin/env bash

DIR="."
previousVersion="1.1.0"
newVersion="1.1.1"

function replaceInFile () {
  filePath=$1

  if grep -q "$previousVersion" "$fname"; then
    echo "-> found something to replace..."
    vim -c "%s/$previousVersion/$newVersion/gc" -c 'wq' "$fname"
  else
    echo "-> Nothing to replace."
  fi

}

echo "You are trying to bump the version number."
echo "This process should only be done in the release/x-y-z branch, with a proper version number."
echo "Please remember to update the 'previousVersion' and 'newVersion' variables (currenty hardcoded on purpose)."
echo "Current values:"
echo "* previousVersion: $previousVersion"
echo "* newVersion: $newVersion"
echo ""

echo "Looping through docs ($DIR/*.md) ..."
for fname in $(find "$DIR" -iname "*.md" -print); do
#for fname in **/**.md; do
  read -p "Inspect file $fname ? (Y/n/q) ";

  if [ "$REPLY" == "n" ];
  then
    echo "Skipped."
  elif [ "$REPLY" == "q" ];
  then
    echo "Exiting"
    exit 0
  else
    replaceInFile $fname
  fi
done

echo ""

fname='gradle.properties'
read -p "Should we update $fname ? (Y/n/q) ";

if [ "$REPLY" == "n" -o "$REPLY" == "q" ];
then
  echo "Exiting"
  exit 0
else
    replaceInFile $fname
fi

echo ""
echo "Please take the time to go through all remaining matchs of the previous version !"
read -p "-> grep  --exclude-dir=.git --exclude-dir=build --exclude-dir=out -nr $previousVersion $DIR"
grep  --exclude-dir=.git --exclude-dir=build --exclude-dir=out -nr $previousVersion $DIR