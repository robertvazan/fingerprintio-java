# This script generates and updates project configuration files.

# Run this script with rvscaffold in PYTHONPATH
import rvscaffold as scaffold

class Project(scaffold.Java):
    def script_path_text(self): return __file__
    def repository_name(self): return 'fingerprintio-java'
    def pretty_name(self): return 'FingerprintIO for Java'
    def pom_name(self): return 'FingerprintIO'
    def pom_description(self): return 'Implementation of fingerprint template formats published by ANSI and ISO.'
    def inception_year(self): return 2019
    def stagean_annotations(self): return True
    
    def dependencies(self):
        yield from super().dependencies()
        yield self.use_noexception()
        yield self.use_junit()
        yield self.use_commons_io('test')
        yield self.use_jackson('test')
    
    def javadoc_links(self):
        yield from super().javadoc_links()
        yield 'https://noexception.machinezoo.com/javadoc/'

Project().generate()
