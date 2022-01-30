# This script generates and updates project configuration files.

# We are assuming that project-config is available in sibling directory.
# Checkout from https://github.com/robertvazan/project-config
import os.path
import sys
sys.path.append(os.path.normpath(os.path.join(__file__, '../../../project-config/src')))

from java import *

project_script_path = __file__
repository_name = lambda: 'fingerprintio-java'
pretty_name = lambda: 'FingerprintIO for Java'
pom_subgroup = lambda: 'fingerprintio'
pom_artifact = lambda: 'fingerprintio'
pom_name = lambda: 'FingerprintIO'
pom_description = lambda: 'Implementation of fingerprint template formats published by ANSI and ISO.'
inception_year = lambda: 2019
jdk_version = lambda: 11

def dependencies():
    use_noexception()
    use_junit()
    use_commons_io('test')
    use_jackson('test')
    use_slf4j_test()

generate(globals())
