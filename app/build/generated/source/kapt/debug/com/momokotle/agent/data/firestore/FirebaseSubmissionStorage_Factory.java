package com.momokotle.agent.data.firestore;

import com.google.firebase.storage.FirebaseStorage;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class FirebaseSubmissionStorage_Factory implements Factory<FirebaseSubmissionStorage> {
  private final Provider<FirebaseStorage> storageProvider;

  public FirebaseSubmissionStorage_Factory(Provider<FirebaseStorage> storageProvider) {
    this.storageProvider = storageProvider;
  }

  @Override
  public FirebaseSubmissionStorage get() {
    return newInstance(storageProvider.get());
  }

  public static FirebaseSubmissionStorage_Factory create(
      Provider<FirebaseStorage> storageProvider) {
    return new FirebaseSubmissionStorage_Factory(storageProvider);
  }

  public static FirebaseSubmissionStorage newInstance(FirebaseStorage storage) {
    return new FirebaseSubmissionStorage(storage);
  }
}
