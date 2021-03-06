/*
 * -\-\-
 * Mobius
 * --
 * Copyright (c) 2017-2020 Spotify AB
 * --
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -/-/-
 */
package com.spotify.mobius;

import com.spotify.mobius.disposables.Disposable;
import com.spotify.mobius.functions.Consumer;
import javax.annotation.Nonnull;

class EventSourceConnectable<M, E> implements Connectable<M, E> {

  public static <M, E> Connectable<M, E> create(EventSource<E> source) {
    return new EventSourceConnectable<>(source);
  }

  private final EventSource<E> eventSource;

  private EventSourceConnectable(EventSource<E> source) {
    eventSource = source;
  }

  @Nonnull
  @Override
  public Connection<M> connect(final Consumer<E> output) throws ConnectionLimitExceededException {
    final Disposable disposable = eventSource.subscribe(output);
    return new Connection<M>() {
      @Override
      public synchronized void accept(M value) {}

      @Override
      public synchronized void dispose() {
        disposable.dispose();
      }
    };
  }
}
