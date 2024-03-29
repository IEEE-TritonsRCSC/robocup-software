// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ssl_simulation_error.proto

package proto.simulation;

public final class SslSimulationError {
  private SslSimulationError() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface SimulatorErrorOrBuilder extends
      // @@protoc_insertion_point(interface_extends:proto.simulation.SimulatorError)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * Unique code of the error for automatic handling on client side
     * </pre>
     *
     * <code>optional string code = 1;</code>
     * @return Whether the code field is set.
     */
    boolean hasCode();
    /**
     * <pre>
     * Unique code of the error for automatic handling on client side
     * </pre>
     *
     * <code>optional string code = 1;</code>
     * @return The code.
     */
    java.lang.String getCode();
    /**
     * <pre>
     * Unique code of the error for automatic handling on client side
     * </pre>
     *
     * <code>optional string code = 1;</code>
     * @return The bytes for code.
     */
    com.google.protobuf.ByteString
        getCodeBytes();

    /**
     * <pre>
     * Human readable description of the error
     * </pre>
     *
     * <code>optional string message = 2;</code>
     * @return Whether the message field is set.
     */
    boolean hasMessage();
    /**
     * <pre>
     * Human readable description of the error
     * </pre>
     *
     * <code>optional string message = 2;</code>
     * @return The message.
     */
    java.lang.String getMessage();
    /**
     * <pre>
     * Human readable description of the error
     * </pre>
     *
     * <code>optional string message = 2;</code>
     * @return The bytes for message.
     */
    com.google.protobuf.ByteString
        getMessageBytes();
  }
  /**
   * <pre>
   * Errors in the simulator
   * </pre>
   *
   * Protobuf type {@code proto.simulation.SimulatorError}
   */
  public static final class SimulatorError extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:proto.simulation.SimulatorError)
      SimulatorErrorOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use SimulatorError.newBuilder() to construct.
    private SimulatorError(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private SimulatorError() {
      code_ = "";
      message_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new SimulatorError();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private SimulatorError(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              code_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              message_ = bs;
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.simulation.SslSimulationError.internal_static_proto_simulation_SimulatorError_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.simulation.SslSimulationError.internal_static_proto_simulation_SimulatorError_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.simulation.SslSimulationError.SimulatorError.class, proto.simulation.SslSimulationError.SimulatorError.Builder.class);
    }

    private int bitField0_;
    public static final int CODE_FIELD_NUMBER = 1;
    private volatile java.lang.Object code_;
    /**
     * <pre>
     * Unique code of the error for automatic handling on client side
     * </pre>
     *
     * <code>optional string code = 1;</code>
     * @return Whether the code field is set.
     */
    @java.lang.Override
    public boolean hasCode() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <pre>
     * Unique code of the error for automatic handling on client side
     * </pre>
     *
     * <code>optional string code = 1;</code>
     * @return The code.
     */
    @java.lang.Override
    public java.lang.String getCode() {
      java.lang.Object ref = code_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          code_ = s;
        }
        return s;
      }
    }
    /**
     * <pre>
     * Unique code of the error for automatic handling on client side
     * </pre>
     *
     * <code>optional string code = 1;</code>
     * @return The bytes for code.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getCodeBytes() {
      java.lang.Object ref = code_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        code_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int MESSAGE_FIELD_NUMBER = 2;
    private volatile java.lang.Object message_;
    /**
     * <pre>
     * Human readable description of the error
     * </pre>
     *
     * <code>optional string message = 2;</code>
     * @return Whether the message field is set.
     */
    @java.lang.Override
    public boolean hasMessage() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <pre>
     * Human readable description of the error
     * </pre>
     *
     * <code>optional string message = 2;</code>
     * @return The message.
     */
    @java.lang.Override
    public java.lang.String getMessage() {
      java.lang.Object ref = message_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          message_ = s;
        }
        return s;
      }
    }
    /**
     * <pre>
     * Human readable description of the error
     * </pre>
     *
     * <code>optional string message = 2;</code>
     * @return The bytes for message.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getMessageBytes() {
      java.lang.Object ref = message_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        message_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) != 0)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, code_);
      }
      if (((bitField0_ & 0x00000002) != 0)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, message_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) != 0)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, code_);
      }
      if (((bitField0_ & 0x00000002) != 0)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, message_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof proto.simulation.SslSimulationError.SimulatorError)) {
        return super.equals(obj);
      }
      proto.simulation.SslSimulationError.SimulatorError other = (proto.simulation.SslSimulationError.SimulatorError) obj;

      if (hasCode() != other.hasCode()) return false;
      if (hasCode()) {
        if (!getCode()
            .equals(other.getCode())) return false;
      }
      if (hasMessage() != other.hasMessage()) return false;
      if (hasMessage()) {
        if (!getMessage()
            .equals(other.getMessage())) return false;
      }
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasCode()) {
        hash = (37 * hash) + CODE_FIELD_NUMBER;
        hash = (53 * hash) + getCode().hashCode();
      }
      if (hasMessage()) {
        hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
        hash = (53 * hash) + getMessage().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static proto.simulation.SslSimulationError.SimulatorError parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(proto.simulation.SslSimulationError.SimulatorError prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * Errors in the simulator
     * </pre>
     *
     * Protobuf type {@code proto.simulation.SimulatorError}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:proto.simulation.SimulatorError)
        proto.simulation.SslSimulationError.SimulatorErrorOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return proto.simulation.SslSimulationError.internal_static_proto_simulation_SimulatorError_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return proto.simulation.SslSimulationError.internal_static_proto_simulation_SimulatorError_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                proto.simulation.SslSimulationError.SimulatorError.class, proto.simulation.SslSimulationError.SimulatorError.Builder.class);
      }

      // Construct using proto.simulation.SslSimulationError.SimulatorError.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        code_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        message_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.simulation.SslSimulationError.internal_static_proto_simulation_SimulatorError_descriptor;
      }

      @java.lang.Override
      public proto.simulation.SslSimulationError.SimulatorError getDefaultInstanceForType() {
        return proto.simulation.SslSimulationError.SimulatorError.getDefaultInstance();
      }

      @java.lang.Override
      public proto.simulation.SslSimulationError.SimulatorError build() {
        proto.simulation.SslSimulationError.SimulatorError result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public proto.simulation.SslSimulationError.SimulatorError buildPartial() {
        proto.simulation.SslSimulationError.SimulatorError result = new proto.simulation.SslSimulationError.SimulatorError(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          to_bitField0_ |= 0x00000001;
        }
        result.code_ = code_;
        if (((from_bitField0_ & 0x00000002) != 0)) {
          to_bitField0_ |= 0x00000002;
        }
        result.message_ = message_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof proto.simulation.SslSimulationError.SimulatorError) {
          return mergeFrom((proto.simulation.SslSimulationError.SimulatorError)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(proto.simulation.SslSimulationError.SimulatorError other) {
        if (other == proto.simulation.SslSimulationError.SimulatorError.getDefaultInstance()) return this;
        if (other.hasCode()) {
          bitField0_ |= 0x00000001;
          code_ = other.code_;
          onChanged();
        }
        if (other.hasMessage()) {
          bitField0_ |= 0x00000002;
          message_ = other.message_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        proto.simulation.SslSimulationError.SimulatorError parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (proto.simulation.SslSimulationError.SimulatorError) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object code_ = "";
      /**
       * <pre>
       * Unique code of the error for automatic handling on client side
       * </pre>
       *
       * <code>optional string code = 1;</code>
       * @return Whether the code field is set.
       */
      public boolean hasCode() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <pre>
       * Unique code of the error for automatic handling on client side
       * </pre>
       *
       * <code>optional string code = 1;</code>
       * @return The code.
       */
      public java.lang.String getCode() {
        java.lang.Object ref = code_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            code_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * Unique code of the error for automatic handling on client side
       * </pre>
       *
       * <code>optional string code = 1;</code>
       * @return The bytes for code.
       */
      public com.google.protobuf.ByteString
          getCodeBytes() {
        java.lang.Object ref = code_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          code_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * Unique code of the error for automatic handling on client side
       * </pre>
       *
       * <code>optional string code = 1;</code>
       * @param value The code to set.
       * @return This builder for chaining.
       */
      public Builder setCode(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        code_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Unique code of the error for automatic handling on client side
       * </pre>
       *
       * <code>optional string code = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearCode() {
        bitField0_ = (bitField0_ & ~0x00000001);
        code_ = getDefaultInstance().getCode();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Unique code of the error for automatic handling on client side
       * </pre>
       *
       * <code>optional string code = 1;</code>
       * @param value The bytes for code to set.
       * @return This builder for chaining.
       */
      public Builder setCodeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        code_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object message_ = "";
      /**
       * <pre>
       * Human readable description of the error
       * </pre>
       *
       * <code>optional string message = 2;</code>
       * @return Whether the message field is set.
       */
      public boolean hasMessage() {
        return ((bitField0_ & 0x00000002) != 0);
      }
      /**
       * <pre>
       * Human readable description of the error
       * </pre>
       *
       * <code>optional string message = 2;</code>
       * @return The message.
       */
      public java.lang.String getMessage() {
        java.lang.Object ref = message_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            message_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * Human readable description of the error
       * </pre>
       *
       * <code>optional string message = 2;</code>
       * @return The bytes for message.
       */
      public com.google.protobuf.ByteString
          getMessageBytes() {
        java.lang.Object ref = message_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          message_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * Human readable description of the error
       * </pre>
       *
       * <code>optional string message = 2;</code>
       * @param value The message to set.
       * @return This builder for chaining.
       */
      public Builder setMessage(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        message_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Human readable description of the error
       * </pre>
       *
       * <code>optional string message = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearMessage() {
        bitField0_ = (bitField0_ & ~0x00000002);
        message_ = getDefaultInstance().getMessage();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Human readable description of the error
       * </pre>
       *
       * <code>optional string message = 2;</code>
       * @param value The bytes for message to set.
       * @return This builder for chaining.
       */
      public Builder setMessageBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        message_ = value;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:proto.simulation.SimulatorError)
    }

    // @@protoc_insertion_point(class_scope:proto.simulation.SimulatorError)
    private static final proto.simulation.SslSimulationError.SimulatorError DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new proto.simulation.SslSimulationError.SimulatorError();
    }

    public static proto.simulation.SslSimulationError.SimulatorError getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<SimulatorError>
        PARSER = new com.google.protobuf.AbstractParser<SimulatorError>() {
      @java.lang.Override
      public SimulatorError parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new SimulatorError(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<SimulatorError> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<SimulatorError> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public proto.simulation.SslSimulationError.SimulatorError getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_simulation_SimulatorError_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_simulation_SimulatorError_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\032ssl_simulation_error.proto\022\020proto.simu" +
      "lation\"/\n\016SimulatorError\022\014\n\004code\030\001 \001(\t\022\017" +
      "\n\007message\030\002 \001(\tB8Z6github.com/RoboCup-SS" +
      "L/ssl-simulation-protocol/pkg/sim"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_proto_simulation_SimulatorError_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_proto_simulation_SimulatorError_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_proto_simulation_SimulatorError_descriptor,
        new java.lang.String[] { "Code", "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
