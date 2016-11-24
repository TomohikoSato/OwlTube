package com.example.tomohiko_sato.owltube.domain.util.TupleUtil;

/**
 * JavaでTupleを実現したクラス
 */
public class TupleUtil {
	public static class Pair<A, B> {
		public final A car;
		public final B cdr;

		public Pair(A car, B cdr) {
			this.car = car;
			this.cdr = cdr;
		}

		private static boolean eq(Object o1, Object o2) {
			return o1 == null ? o2 == null : o1.equals(o2);
		}

		private static int hc(Object o) {
			return o == null ? 0 : o.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Pair)) return false;
			Pair<?, ?> rhs = (Pair<?, ?>) o;
			return eq(car, rhs.car) && eq(cdr, rhs.cdr);
		}

		@Override
		public int hashCode() {
			return hc(car) ^ hc(cdr);
		}
	}

	public static class Tuple1<A> extends Pair<A, Object> {
		public Tuple1(A a) {
			super(a, null);
		}
	}

	public static class Tuple2<A, B> extends Pair<A, Tuple1<B>> {
		public Tuple2(A a, B b) {
			super(a, new Tuple1<>(b));
		}
	}
}
