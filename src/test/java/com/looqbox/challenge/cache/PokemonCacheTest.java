package com.looqbox.challenge.cache;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PokemonCacheTest {

    @InjectMocks
    PokemonCache cache;

    @Nested
    class IsValidTest {

        @Test
        void testShouldReturnFalseWhenCacheIsEmpty() {
            boolean result = cache.isValid();

            assertThat(result).isFalse();
        }

        @Test
        void testShouldReturnTrueAfterSetPokemons() {
            cache.setPokemons(List.of("pikachu", "bulbasaur"));

            boolean result = cache.isValid();

            assertThat(result).isTrue();
        }

        @Test
        void testShouldReturnFalseWhenTtlHasExpired() throws Exception {
            cache.setPokemons(List.of("pikachu"));

            Field lastFetchTimeMsField = PokemonCache.class.getDeclaredField("lastFetchTimeMs");
            lastFetchTimeMsField.setAccessible(true);
            lastFetchTimeMsField.set(cache, 0L);

            boolean result = cache.isValid();

            assertThat(result).isFalse();
        }
    }

    @Nested
    class GetPokemonsTest {

        @Test
        void testShouldReturnNullWhenCacheIsEmpty() {
            List<String> result = cache.getPokemons();

            assertThat(result).isNull();
        }

        @Test
        void testShouldReturnStoredPokemonsAfterSet() {
            cache.setPokemons(List.of("pikachu", "bulbasaur"));

            List<String> result = cache.getPokemons();

            assertThat(result).containsExactly("pikachu", "bulbasaur");
        }
    }

    @Nested
    class SetPokemonsTest {

        @Test
        void testShouldMakeCacheValidAfterSet() {
            cache.setPokemons(List.of("pikachu"));

            assertThat(cache.isValid()).isTrue();
        }

        @Test
        void testShouldStoreImmutableCopyOfInput() {
            List<String> input = new ArrayList<>(List.of("pikachu"));
            cache.setPokemons(input);

            input.add("raichu");

            assertThat(cache.getPokemons()).containsExactly("pikachu");
        }

        @Test
        void testShouldReturnUnmodifiableList() {
            cache.setPokemons(List.of("pikachu"));

            List<String> result = cache.getPokemons();

            assertThatThrownBy(() -> result.add("raichu"))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
