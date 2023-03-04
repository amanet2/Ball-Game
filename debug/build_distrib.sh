pkg_dir="$(cd $(dirname "$1");pwd)"/../../pkg_Ball-Game-Mac-test
rm -rf ${pkg_dir}
mkdir ${pkg_dir}
mkdir ${pkg_dir}/bin
mkdir ${pkg_dir}/lib
mkdir ${pkg_dir}/pkg
cp -r "$(cd $(dirname "$1");pwd)"/../bin ${pkg_dir}/bin
cp -r "$(cd $(dirname "$1");pwd)"/../lib ${pkg_dir}/lib
cp -r "$(cd $(dirname "$1");pwd)"/../pkg ${pkg_dir}/pkg
cp "$(cd $(dirname "$1");pwd)"/../Readme.txt ${pkg_dir}/Readme.txt
cp "$(cd $(dirname "$1");pwd)"/../Ball-Game-Mac ${pkg_dir}/Ball-Game-Mac
cp "$(cd $(dirname "$1");pwd)"/../Ball-Game-Mapmaker-Mac ${pkg_dir}/Ball-Game-Mapmaker-Mac
rm -f ${pkg_dir}.zip
zip -r ${pkg_dir}.zip ${pkg_dir}