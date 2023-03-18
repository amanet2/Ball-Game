pkg_dir="$(cd $(dirname "$1");pwd)"/../../pkg_Ball-Game-Mac
rm -rf ${pkg_dir}
mkdir ${pkg_dir}
cp -r "$(cd $(dirname "$1");pwd)"/../bin ${pkg_dir}
cp -r "$(cd $(dirname "$1");pwd)"/../lib ${pkg_dir}
cp -r "$(cd $(dirname "$1");pwd)"/../pkg ${pkg_dir}
cp "$(cd $(dirname "$1");pwd)"/../Readme.txt ${pkg_dir}
cp "$(cd $(dirname "$1");pwd)"/../Ball-Game-Mac ${pkg_dir}
cp "$(cd $(dirname "$1");pwd)"/../Ball-Game-Mapmaker-Mac ${pkg_dir}
rm -f ${pkg_dir}.zip
zip -qr ${pkg_dir}.zip ${pkg_dir}