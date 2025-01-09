package kr.techit.lion.convention.extension

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider

internal fun VersionCatalog.getBundle(bundleName: String): Provider<ExternalModuleDependencyBundle> =
    findBundle(bundleName).orElseThrow {
        NoSuchElementException("Bundle with name $bundleName not found in the catalog")
    }

internal fun VersionCatalog.getLibrary(libraryName: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(libraryName).orElseThrow {
        NoSuchElementException("Library with name $libraryName not found in the catalog")
    }

internal fun VersionCatalog.getVersion(versionName: String): VersionConstraint =
    findVersion(versionName).orElseThrow {
        NoSuchElementException("Version with name $versionName not found in the catalog")
    }