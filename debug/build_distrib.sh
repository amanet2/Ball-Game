pkg_dir="$(cd $(dirname "$1");pwd)"/../../pkg_ballmaster_mac
rm -rf ${pkg_dir}
mkdir ${pkg_dir}
cp -r "$(cd $(dirname "$1");pwd)"/../bin ${pkg_dir}
cp -r "$(cd $(dirname "$1");pwd)"/../pkg ${pkg_dir}
cp "$(cd $(dirname "$1");pwd)"/../Readme.txt ${pkg_dir}
rm -f ${pkg_dir}.zip
zip -qr ${pkg_dir}.zip ${pkg_dir}