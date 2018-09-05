package cn.dreamcatchers.fastfile.tool;

import com.google.common.hash.HashCode;
import com.google.common.io.BaseEncoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * BY MEMORYNOTFOUND · PUBLISHED MARCH 2, 2015 · UPDATED JUNE 23, 2015
 * https://memorynotfound.com/calculate-file-checksum-java/
 */
public enum Hash {

    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private String name;

    Hash(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String checksum(File input) {
        try (InputStream in = new FileInputStream(input)) {
            var digest = MessageDigest.getInstance(getName());
            var block = new byte[4096];
            int length;
            while ((length = in.read(block)) > 0) {
                digest.update(block, 0, length);
            }
            return HashCode.fromBytes(digest.digest()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
