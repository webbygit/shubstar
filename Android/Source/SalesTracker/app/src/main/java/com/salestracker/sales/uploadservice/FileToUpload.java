package com.salestracker.sales.uploadservice;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Represents a file to upload.
 * 
 * @author alexbbb (Alex Gotev)
 * @author eliasnaur
 * 
 */
class FileToUpload implements Parcelable {

    private static final String NEW_LINE = "\r\n";

    private final File file;
    private final String fileName;
    private final String paramName;
    private String contentType;

    /**
     * Create a new {@link FileToUpload} object.
     * 
     * @param path absolute path to the file
     * @param parameterName parameter name to use in the multipart form
     * @param contentType content type of the file to send
     */
    public FileToUpload(final String path, final String parameterName, final String fileName, final String contentType) {
        this.file = new File(path);
        this.paramName = parameterName;
        this.contentType = contentType;

        if (fileName == null || "".equals(fileName)) {
            this.fileName = this.file.getName();
        } else {
            this.fileName = fileName;
        }
    }

    public final InputStream getStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public byte[] getMultipartHeader() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();

        builder.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"; filename=\"")
                .append(fileName).append("\"").append(NEW_LINE);

        if (contentType == null) {
            contentType = ContentType.APPLICATION_OCTET_STREAM;
        }

        builder.append("Content-Type: ").append(contentType).append(NEW_LINE).append(NEW_LINE);

        return builder.toString().getBytes("US-ASCII");
    }

    public long length() {
        return file.length();
    }

    /**
     * Get the total number of bytes needed by this file in the HTTP/Multipart request, considering that to send each
     * file there is some overhead due to some bytes needed for the boundary and some bytes needed for the multipart
     * headers
     * 
     * @param boundaryBytesLength length in bytes of the multipart boundary
     * @return total number of bytes needed by this file in the HTTP/Multipart request
     * @throws UnsupportedEncodingException
     */
    public long getTotalMultipartBytes(long boundaryBytesLength) throws UnsupportedEncodingException {
        return boundaryBytesLength + getMultipartHeader().length + file.length();
    }

    // This is used to regenerate the object.
    // All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<FileToUpload> CREATOR = new Creator<FileToUpload>() {
        @Override
        public FileToUpload createFromParcel(final Parcel in) {
            return new FileToUpload(in);
        }

        @Override
        public FileToUpload[] newArray(final int size) {
            return new FileToUpload[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int arg1) {
        parcel.writeString(file.getAbsolutePath());
        parcel.writeString(paramName);
        parcel.writeString(contentType);
        parcel.writeString(fileName);
    }

    private FileToUpload(Parcel in) {
        file = new File(in.readString());
        paramName = in.readString();
        contentType = in.readString();
        fileName = in.readString();
    }
}
